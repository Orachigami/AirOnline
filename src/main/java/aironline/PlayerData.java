package main.java.aironline;

import java.util.UUID;

/**
 * Represents valueable player info be serialized
 * @author Orachigami
 */
public final class PlayerData {
    private final UUID uuid;
    private String name;
    private long lastPlayedTime;
    private long dayPlayedTime;
    private long weekPlayedTime;
    private long monthPlayedTime;
    private long totalPlayedTime;
    private long cooldown;
    private boolean online;
    
    public PlayerData(UUID uuid, String name, long startTime, boolean online) {
        this.uuid = uuid;
        this.name = name;
        lastPlayedTime = startTime;
        monthPlayedTime = 0;
        weekPlayedTime = 0;
        dayPlayedTime = 0;
        totalPlayedTime = 0;
        cooldown = 0;
        this.online = online;
    }
    
    public PlayerData(UUID uuid, String name, long startTime, long dayPlayedTime, long weekPlayedTime, long monthPlayedTime, long totalPlayedTime, boolean online) {
        this.uuid = uuid;
        this.name = name;
        this.lastPlayedTime = startTime;
        this.dayPlayedTime = dayPlayedTime;
        this.weekPlayedTime = weekPlayedTime;
        this.monthPlayedTime = monthPlayedTime;
        this.totalPlayedTime = totalPlayedTime;
        cooldown = 0;
        this.online = online;
    }

    public long getLastPlayedTime() {
        return lastPlayedTime;
    }

    public void setLastPlayedTime(long lastPlayedTime) {
        this.lastPlayedTime = lastPlayedTime;
    }

    public long getTotalPlayedTime() {
        return totalPlayedTime;
    }

    public void setTotalPlayedTime(long totalPlayedTime) {
        this.totalPlayedTime = totalPlayedTime;
    }
    
    public UUID getUUID() {
        return uuid;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDayPlayedTime() {
        return dayPlayedTime;
    }

    public void setDayPlayedTime(long dayPlayedTime) {
        this.dayPlayedTime = dayPlayedTime;
    }

    public long getWeekPlayedTime() {
        return weekPlayedTime;
    }

    public void setWeekPlayedTime(long weekPlayedTime) {
        this.weekPlayedTime = weekPlayedTime;
    }

    public long getMonthPlayedTime() {
        return monthPlayedTime;
    }

    public void setMonthPlayedTime(long monthPlayedTime) {
        this.monthPlayedTime = monthPlayedTime;
    }

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }
}
