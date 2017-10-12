/*
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.rps.bdd.page;

import java.io.IOException;

import org.junit.Assert;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 21/09/2017
 */
public class Main {

    private final WebClient webClient;

    /**
     * @param webDriver
     */
    public Main(final WebClient webClient) {
        this.webClient = webClient;
    }

    public void open(final String url) {
        Assert.assertNotNull(url);
        try {
            final HtmlPage mainPage = this.webClient.getPage(url);
            webClient.waitForBackgroundJavaScript(1000 * 5);
            Assert.assertEquals(200, mainPage.getWebResponse().getStatusCode());
            Assert.assertEquals("OK", mainPage.getWebResponse().getStatusMessage());
            Assert.assertEquals("text/html", mainPage.getWebResponse().getContentType());
            Assert.assertEquals("Rock, Paper and Scissors", mainPage.getTitleText());
            Assert.assertEquals("", mainPage.getElementById("note").asText());
            Assert.assertEquals("Max Score: --", mainPage.getElementById("maxScore").asText());
            Assert.assertEquals("You", mainPage.getElementById("name1").asText());
            Assert.assertEquals("0", mainPage.getElementById("score1").asText());
            Assert.assertEquals("Computer", mainPage.getElementById("name2").asText());
            Assert.assertEquals("0", mainPage.getElementById("score2").asText());
            Assert.assertEquals("Human vs Computer", mainPage.getElementById("0").asText());
            Assert.assertEquals("Computer vs Computer", mainPage.getElementById("1").asText());
        } catch (Exception e) {
            Assert.assertNull(e.getMessage(), e);
        }
    }

    public void clickButton(final String buttonText) {
        Assert.assertNotNull(buttonText);
        final HtmlButton button = currentPage().getHtmlElementById("Human vs Computer".equals(buttonText) ? "0" : "1");
        try {
            button.click();
        } catch (IOException e) {
            Assert.assertNull(e.getMessage(), e);
        }
    }

    public void maxScore(String maxScore) {
        wait(2);
        Assert.assertEquals(maxScore, currentPage().getElementById("maxScoreValue").asText());
    }

    public void scoreTitles(final String score1, final String score2) {
        Assert.assertNotNull(score1);
        Assert.assertNotNull(score2);
        Assert.assertEquals(score1, currentPage().getElementById("name1").asText());
        Assert.assertEquals(score2, currentPage().getElementById("name2").asText());
    }

    public void scoreValues(String value1, String value2) {
        Assert.assertEquals(value1, currentPage().getElementById("score1").asText());
        Assert.assertEquals(value2, currentPage().getElementById("score2").asText());
    }
    
    private HtmlPage currentPage() {
        return (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();
    }
    
    
    private void wait(int seconds) {
        try {
            Thread.sleep(seconds);
        } catch (Exception e) {
            Assert.assertNull(e.getMessage(), e);
        }
    }
}