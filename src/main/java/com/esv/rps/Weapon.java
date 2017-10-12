/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.rps;

import java.util.Random;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 18/09/2017
 */
public enum Weapon {

    ROCK, PAPER, SCISSORS;
    
    private static final Random random = new Random();
    public final String name = name().toLowerCase();

    /**
     * @param weapon
     * @return
     */
    public int compare(final Weapon weapon) {
        if (this != weapon) {
            switch (this) {
            case ROCK:
                return (SCISSORS  == weapon? 1 : -1);
            case PAPER:
                return (ROCK  == weapon? 1 : -1);
            case SCISSORS:
                return (PAPER == weapon? 1 : -1);
            }
        }
        return 0;
    }
    
    /**
     * @param weapon
     * @return
     */
    public String getMessageCode(final Weapon weapon) {
        String messageCode = "0";
        if (this != weapon) {
            switch (this) {
            case ROCK:
                messageCode = SCISSORS  == weapon? "1" : "2";
                break;
            case PAPER:
                messageCode = ROCK  == weapon? "3" : "4";
                break;
            case SCISSORS:
                messageCode = PAPER == weapon? "5" : "6";
                break;
            }
        }
        return messageCode;
    }
    
    /**
     * @return
     */
    public static Weapon randomWeapon() {
        return values()[Weapon.random.nextInt(values().length)];
    }
}