/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.rps;

import com.esv.net.server.MicroServer;
import com.esv.utile.logging.Logger;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 18/09/2017
 */
public class Application {

    private static final Logger LOGGER = Logger.getLogger(Application.class);

    /**
     * <p>Starts the micro server</p>
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            MicroServer.run();
        } catch (Throwable t) {
            LOGGER.fatal("Unexpected micro server error", t);
            System.exit(1);
        }
    }
}