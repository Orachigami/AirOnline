/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aironline;

import java.util.UUID;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

/**
 *
 * @author Orachigami
 */
public class PlayerEventListenerTest {
    
    private PlayerEventListener listener;
    
    @Mock ProxiedPlayer proxiedPlayer;
    @Mock Server server;
    @Mock ServerInfo serverInfo;
    @Mock ProxyServer proxyServer;
    UUID uuid = UUID.fromString("25e0bba4-9813-373d-85fd-886435e6e2e3");
    
    public PlayerEventListenerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    
    @Before
    public void setUp() {
        server = mock(Server.class);
        serverInfo = mock(ServerInfo.class);
        proxyServer = mock(ProxyServer.class);
        proxiedPlayer = mock(ProxiedPlayer.class);
        
        when(server.getInfo()).thenReturn(serverInfo);
        when(serverInfo.getName()).thenReturn("SERVER");
        when(proxyServer.getName()).thenReturn("SERVER");
        when(proxiedPlayer.getName()).thenReturn("Orachigami");
        when(proxiedPlayer.getUniqueId()).thenReturn(uuid);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of onJoin method, of class PlayerEventListener.
     */
    @Test
    public void testOnFirstJoin() {
        PlayerDataHashMap map = new PlayerDataHashMap();
        listener = new PlayerEventListener(new String[]{}, map);
        ServerConnectedEvent e = new ServerConnectedEvent(proxiedPlayer, server);
        listener.onJoin(e);
        long now = System.currentTimeMillis();
        PlayerData player = map.get(uuid);
        assertNotNull(player);
        assertTrue(player.isOnline());
        assertEquals(uuid, player.getUUID());
        assertEquals("Orachigami", player.getName());
        assertEquals(now, player.getLastPlayedTime());
    }

    /**
     * Test of onJoin method, of class PlayerEventListener.
     */
    @Test
    public void testOnSecondJoin() {
        long now = System.currentTimeMillis();
        PlayerDataHashMap map = new PlayerDataHashMap();
        PlayerData player = new PlayerData(uuid, "Orachigami", now - 10000L, false);
        map.put(uuid, player);
        listener = new PlayerEventListener(new String[]{}, map);
        ServerConnectedEvent e = new ServerConnectedEvent(proxiedPlayer, server);
        listener.onJoin(e);
        assertTrue(player.isOnline());
        assertEquals(now, player.getLastPlayedTime());
    }

    /**
     * Test of onJoin method, of class PlayerEventListener.
     */
    @Test
    public void testOnJoinFilter() {
        PlayerDataHashMap map = new PlayerDataHashMap();
        listener = new PlayerEventListener(new String[]{"SERVER"}, map);
        ServerConnectedEvent e = new ServerConnectedEvent(proxiedPlayer, server);
        listener.onJoin(e);
        assertTrue(map.getValues().isEmpty());
    }

    /**
     * Test of onQuit method, of class PlayerEventListener.
     */
    @Test
    public void testOnQuit() {
        PlayerDataHashMap map = new PlayerDataHashMap();
        long now = System.currentTimeMillis();
        PlayerData player = new PlayerData(uuid, "Orachigami", now - 10000L, true);
        map.put(uuid, player);
        listener = new PlayerEventListener(new String[]{}, map);
        ServerDisconnectEvent e = new ServerDisconnectEvent(proxiedPlayer, serverInfo);
        listener.onQuit(e);
        assertFalse(player.isOnline());
        assertEquals(10000L, player.getTotalPlayedTime());
    }

    /**
     * Test of onQuit method, of class PlayerEventListener.
     */
    @Test
    public void testOnQuitFilter() {
        PlayerDataHashMap map = new PlayerDataHashMap();
        long now = System.currentTimeMillis();
        PlayerData player = new PlayerData(uuid, "Orachigami", now, true);
        map.put(uuid, player);
        listener = new PlayerEventListener(new String[]{"SERVER"}, map);
        ServerDisconnectEvent e = new ServerDisconnectEvent(proxiedPlayer, serverInfo);
        listener.onQuit(e);
        assertTrue(player.isOnline());
    }
}
