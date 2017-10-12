/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.rps;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.esv.net.server.MicroServer;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 18/09/2017
 */
public class Application {

    private static final Logger LOGGER = Logger.getGlobal();

    /**
     * <p>Starts the micro server</p>
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            MicroServer.run();
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Unexpected micro server error", e);
            }
            System.exit(1);
        }
    }
}