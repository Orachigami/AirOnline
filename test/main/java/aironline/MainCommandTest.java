/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aironline;

import java.util.UUID;
import net.md_5.bungee.api.CommandSender;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.Mock;

/**
 *
 * @author Orachigami
 */
public class MainCommandTest {
    
    @Mock CommandSender sender;
    @Mock DatabaseManager manager;
    UUID uuid = UUID.fromString("25e0bba4-9813-373d-85fd-886435e6e2e3");
    
    public MainCommandTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        sender = mock(CommandSender.class);
        manager = mock(DatabaseManager.class);
        long now = System.currentTimeMillis();
        when(manager.getData("Orachigami")).thenReturn(new PlayerData(uuid, "Orachigami", now - 10000L, 10000L, 10000L, 10000L, 10000L, false));
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of execute method, of class MainCommand.
     */
    @Test
    public void testExecute() {
        
    }

    /**
     * Test of getAliases method, of class MainCommand.
     */
    @Test
    public void testGetAliases() {
        System.out.println("getAliases");
        MainCommand instance = null;
        String[] expResult = null;
        String[] result = instance.getAliases();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
