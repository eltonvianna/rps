/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.rps.unit;

import org.junit.Assert;
import org.junit.Test;

import com.esv.rps.Weapon;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 18/09/2017
 */
public class WeaponTest {
    
    @Test
    public void test01TestRockWin() {
        Assert.assertEquals(1, Weapon.ROCK.compare(Weapon.SCISSORS));
    }
    
    @Test
    public void test02TestPaperWin() {
        Assert.assertEquals(1, Weapon.PAPER.compare(Weapon.ROCK));
    }
    
    @Test
    public void test03TestScissorsWin() {
        Assert.assertEquals(1, Weapon.SCISSORS.compare(Weapon.PAPER));
    }
    
    @Test
    public void test04TestRockLose() {
        Assert.assertEquals(-1, Weapon.ROCK.compare(Weapon.PAPER));
    }
    
    @Test
    public void test05TestPaperLose() {
        Assert.assertEquals(-1, Weapon.PAPER.compare(Weapon.SCISSORS));
    }
    
    @Test
    public void test06TestScissorsLose() {
        Assert.assertEquals(-1, Weapon.SCISSORS.compare(Weapon.ROCK));
    }
    
    @Test
    public void test07TestWeaponTie() {
        Assert.assertEquals(0, Weapon.ROCK.compare(Weapon.ROCK));
        Assert.assertEquals(0, Weapon.PAPER.compare(Weapon.PAPER));
        Assert.assertEquals(0, Weapon.SCISSORS.compare(Weapon.SCISSORS));
    }
}