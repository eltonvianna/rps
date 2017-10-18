/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.rps;

import java.util.LinkedHashMap;
import java.util.Map;

import com.esv.net.rest.Get;
import com.esv.net.rest.RestService;
import com.esv.utile.logging.Logger;
import com.esv.utile.utils.PropertiesUtils;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 19/09/2017
 */
@RestService(singleton = true)
public class GameService {
    
    private static final Logger LOGGER = Logger.getLogger(GameService.class);
    
    @Get("/rest/rock")
    public Map<String, String> rock() {
       return move("human", Weapon.ROCK);
    }
    
    @Get("/rest/paper")
    public Map<String, String> paper() {
       return move("human", Weapon.PAPER);
    }
    
    @Get("/rest/scissors")
    public Map<String, String> scissors() {
       return move("human", Weapon.SCISSORS);
    }
    
    @Get("/rest/computer")
    public Map<String, String> computer() {
        return move("computer", Weapon.randomWeapon());
    }
    
    @SuppressWarnings("serial")
    @Get("/rest/configuration")
    public Map<String, String> configuration() {
        final String maxScore = PropertiesUtils.getStringProperty("rps.max.score", "7");
        final String noteTimeout = PropertiesUtils.getStringProperty("rps.note.timeout", "1e3");
        final String wellcomeMessages = PropertiesUtils.getStringProperty("rps.wellcome.messages", "5");
        LOGGER.debug(() -> "Max score: " + maxScore);
        return new LinkedHashMap<String,String>() {{
            put("maxScore", maxScore);
            put("noteTimeout", noteTimeout);
            put("wellcomeMessages", wellcomeMessages);
        }};
    }

    /**
     * @param weapon
     * @return
     */
    @SuppressWarnings("serial")
    private Map<String, String> move(final String player1, final Weapon weapon) {
        final String player2 = "human".equals(player1) ? "computer" : "computer2";
        final Weapon weapon2 = Weapon.randomWeapon();
        final int result = weapon.compare(weapon2);
        LOGGER.debug(() -> "Player1: " + player1 + " moves: " + weapon.name + ", player2: " + player2 + " moves: " + weapon2.name + ". Result: " + result);
        return new LinkedHashMap<String, String>() {{
                put("mode", "human".equals(player1) ? "0" : "1");
                put("result", String.valueOf(result));
                put("resultCode", weapon.getMessageCode(weapon2));
                put("weapon", String.valueOf(result == -1 ? weapon2.ordinal() : weapon.ordinal()));
            }};
    }
}