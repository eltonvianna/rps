/*
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.rps.bdd.page;

import com.gargoylesoftware.htmlunit.WebClient;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 21/09/2017
 */
public class Pages {
 
    private final WebClient webClient;
    private Main main;
    
    /**
     * 
     * @param webClient
     */
    public Pages(final WebClient webClient) {
        super();
        this.webClient = webClient;
    }

    /**
     * @return
     */
    public Main main() {
        if (main == null) {
            main = new Main(this.webClient);
        }
        return main;
    }
}