/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aironline;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Orachigami
 */
public class PlayerDataHashMapTest {
    private PlayerDataHashMap map;
    private UUID uuid;
    
    public PlayerDataHashMapTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        uuid = UUID.fromString("25e0bba4-9813-373d-85fd-886435e6e2e3");
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testPut() {
        PlayerData player = new PlayerData(uuid, "Orachigami", 0, true);
        map = new PlayerDataHashMap();
        map.put(uuid, player);
        assertTrue(map.getValues().contains(player));
    }
    
    @Test
    public void testGet() {
        PlayerData player = new PlayerData(uuid, "Orachigami", 0, true);
        map = new PlayerDataHashMap();
        map.put(uuid, player);
        assertTrue(map.get(uuid).equals(player));
        assertNull(map.get(UUID.fromString("99e0bba4-9813-373d-85fd-886435e6e2e3")));
    }
    
    @Test
    public void testGetValues() {
        PlayerData player = new PlayerData(uuid, "Orachigami", 0, true);
        map = new PlayerDataHashMap();
        map.put(uuid, player);
        assertTrue(map.getValues().contains(player));
    }
    
    /**
     * Test of resetAll method, of class PlayerDataHashMap.
     */
    @Test
    public void testResetAll() {
        String name = "Orachigami";
        long startTime = System.currentTimeMillis();
        map = new PlayerDataHashMap();
        PlayerData player = new PlayerData(uuid, name, startTime, false);
        map.put(uuid, player);
        map.resetAll();
        assertNull(map.get(uuid));
        player.setOnline(true);
        player.setTotalPlayedTime(100);
        map.put(uuid, player);
        map.resetAll();
        assertEquals(player.getTotalPlayedTime(), 0L);
    }
    
}
