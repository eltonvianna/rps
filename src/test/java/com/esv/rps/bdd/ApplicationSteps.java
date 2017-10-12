/*
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.rps.bdd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.stream.Collectors;

import org.jbehave.core.annotations.AfterStories;
import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import org.mockito.Mockito;

import com.esv.rps.Application;
import com.esv.rps.bdd.page.Pages;
import com.google.gson.JsonParser;

/**
 * 
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 21/09/2017
 */
public class ApplicationSteps {

    private final Pages pages;
    private HttpURLConnection connection;
    
    @BeforeStories
    public void setup() throws Exception {
        final Thread thread = Mockito.spy(new Thread(() -> Application.main(null)));
        thread.start();
        Mockito.verify(thread).start();
    }
    
    @AfterStories
    public void teardown() {
        connection.disconnect();
    }

    /**
     * @param pages
     */
    public ApplicationSteps(final Pages pages) {
        super();
        this.pages = pages;
    }

    @Given("user call the Main page with the url: '$url'")
    public void userCallTheMainPageWithTheUrl(final String url){
        pages.main().open(url);
    }
    
    @When("the user click in the button: '$button'")
    public void theUserClickInTheButton(final String button) {
        pages.main().clickButton(button);
    }
    
    @Then("the maximum score is: '$maxScore'")
    public void theMaximumScoreIs(final String maxScore) {
        pages.main().maxScore(maxScore);
    }
    
    @Then("the score 1 title is: '$score1' and the score 2 title is: '$score2'")
    public void scoreTitles(final String score1, final String score2) {
        pages.main().scoreTitles(score1, score2);
    }
    
    @Then("the value of score 1 is: '$value1': and the value of score 2 is: '$value2'")
    public void scoreValues(final String value1, final String value2) {
        pages.main().scoreValues(value1, value2);
    }

    @Given("a '$method' user call the url: '$url'")
    public void aCallToTheURL(final String method, final String url) {
        try {
            openConnection(method, url, null);
            Assert.assertTrue(connection.getContentLength() > 0);
        } catch (Exception e) {
            Assert.assertNull(e.getMessage(), e);
        }
    }
    
    @Given("a '$method' call to the rest service: '$url' returns a '$validation' response")
    public void aCallToTheRestEndpoint(final String method, final String url, final String validation) {
        try {
            openConnection(method, url, "application/json");
            Assert.assertTrue(connection.getContentLength() > 0);
            if ("valid".equals(validation)) {
                try (final BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
                    Assert.assertNotNull(new JsonParser().parse(bf.lines().collect(Collectors.joining("\n"))));
                }
            }
        } catch (Exception e) {
            Assert.assertNull(e.getMessage(), e);
        }
    }

    private void openConnection(final String method, final String url, final String acceptType)
            throws IOException, MalformedURLException, ProtocolException {
        this.connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);
        if (null != acceptType && acceptType.length() > 0) {
            connection.setRequestProperty("Accept", acceptType);
        }
        connection.connect();
    }
    
    @Then("the response code is: '$code'")
    public void theResponseCodeIs(final int code) {
        try {
            Assert.assertEquals(code, connection.getResponseCode());
        } catch (Exception e) {
            Assert.assertNull(e.getMessage(), e);
        }
    }

    @Then("the response message is: '$message'")
    public void theResponseMessageIs(final String message) {
        theResponseMessageIs(message, null);
    }

    @Then("the response message with example is: '$message' '$example'")
    public void theResponseMessageIs(final String message, String example) {
        try {
            Assert.assertEquals(null != example && example.length() > 0 ? message + example : message, connection.getResponseMessage());
        } catch (Exception e) {
            Assert.assertNull(e.getMessage(), e);
        }
    }
    
    @Then("the content type is: '$contentType'")
    public void theContentTypeIs(final String contentType) {
        Assert.assertEquals(contentType, connection.getContentType());
    }
    
    @Then("the json message is: '$json'")
    public void theJsonMessageIs(final String json) {
        try (final BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
            final String jsonResponse = bf.lines().collect(Collectors.joining("\n"));
                Assert.assertNotNull(new JsonParser().parse(jsonResponse));
            Assert.assertEquals(json, jsonResponse);
        } catch (Exception e) {
            Assert.assertNull(e.getMessage(), e);
        }
    }
}