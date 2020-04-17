package main.java.aironline;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Main player event listener
 * @author Orachigami
 */
public final class PlayerEventListener implements Listener {
    private final HashSet<String> serverSet;
    private final PlayerDataHashMap players;
    
    public PlayerEventListener (String[] filter, PlayerDataHashMap players) {
        serverSet = new HashSet();
        this.players = players;
        serverSet.addAll(Arrays.asList(filter));
    }
    
    @EventHandler
    public void onJoin (ServerConnectedEvent e) {
        if (serverSet.contains(e.getServer().getInfo().getName())) return;
        UUID uuid = e.getPlayer().getUniqueId();
        String name = e.getPlayer().getName();
        long now = System.currentTimeMillis();
        PlayerData playerData = players.get(uuid);
        if (playerData == null) {
            players.put(uuid, new PlayerData(uuid, name, now, true));
        } else {
            playerData.setOnline(true);
            playerData.setLastPlayedTime(now);
        }
    }
    
    @EventHandler
    public void onQuit (ServerDisconnectEvent e) {
        if (serverSet.contains(e.getTarget().getName())) return;
        UUID uuid = e.getPlayer().getUniqueId();
        PlayerData playerData = players.get(uuid);
        if (playerData != null) {
            long onlineTime = System.currentTimeMillis() - playerData.getLastPlayedTime();
            playerData.setTotalPlayedTime(playerData.getTotalPlayedTime() + onlineTime);
            playerData.setMonthPlayedTime(playerData.getMonthPlayedTime()+ onlineTime);
            playerData.setWeekPlayedTime(playerData.getWeekPlayedTime() + onlineTime);
            playerData.setDayPlayedTime(playerData.getDayPlayedTime() + onlineTime);
            playerData.setOnline(false);
        }
    }
}
