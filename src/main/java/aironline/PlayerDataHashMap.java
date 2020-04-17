package main.java.aironline;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents concurrent hash map with player data with special methods
 * @author Orachigami
 */
public class PlayerDataHashMap {
    /** Player data map */
    private final ConcurrentHashMap<UUID,PlayerData> map;
    
    /**
     * Default PlayerDataHashMap constructor
     */
    public PlayerDataHashMap() {
        map = new ConcurrentHashMap();
    }
    
    /**
     * Default PlayerDataHashMap constructor
     * @param capacity 
     */
    public PlayerDataHashMap(int capacity) {
        map = new ConcurrentHashMap(capacity);
    }
    
    /**
     * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
     * @param uuid Player unique id
     */
    public PlayerData get (UUID uuid) {
        return map.get(uuid);
    }
    
    /**
     * Maps the specified key to the specified value in this table. Neither the key nor the value can be null.
     * @param uuid Player unique id
     * @param playerData Player data
     */
    public void put (UUID uuid, PlayerData playerData) {
        map.put(uuid, playerData);
    }
    
    /**
     * Returns a Collection view of the values contained in this map.
     */
    public Collection<PlayerData> getValues() {
        return map.values();
    }
    
    /**
     * Resets current play time for online players and removes offline from the list
     */
    public synchronized void resetAll() {
        Iterator<PlayerData> it = map.values().iterator();
        while (it.hasNext()) {
            PlayerData player = it.next();
            if (player.isOnline()) player.setTotalPlayedTime(0);
            else it.remove();
        }
    }
}
