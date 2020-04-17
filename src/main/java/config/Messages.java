package main.java.config;

/**
 * Represents messages to send
 * @author Orachigami
 */
public class Messages {
    private String[] personal;
    private String[] about;
    private String[] cooldown;
    private String[] reload;
    private String[] noPermissions;
    private String[] playerNotFound;
    
    public transient static final String PLAYER = "%player%";
    public transient static final String ONLINE_DAY = "%online_day%";
    public transient static final String ONLINE_WEEK = "%online_week%";
    public transient static final String ONLINE_MONTH = "%online_month%";
    public transient static final String ONLINE_TOTAL = "%online_total%";
    public transient static final String COOLDOWN = "%cooldown%";
    public transient static final String LAST_JOIN = "%last_join%";
    
    private Messages() {}
    
    public static StringBuilder substitute(StringBuilder message, String pattern, String replacement) {
        int index = 0;
        while ((index = message.indexOf(pattern, index)) != -1) {
            message.replace(index, index + pattern.length(), replacement);
        }
        return message;
    }
    
    public static Messages generateDefault() {
        Messages msg = new Messages();
        msg.setPersonal(new String[] {
            "§c§l[!] §aВаша статистика:",
            "§c§l[!] §fНаиграно за день:§c %online_day%",
            "§c§l[!] §fНаиграно за неделю:§c %online_week%",
            "§c§l[!] §fНаиграно за месяц:§c %online_month%",
            "§c§l[!] §fВсего наиграли:§c %online_total%",
        });
        msg.setAbout(new String[] {
            "§c§l[!] §aСтатистика игрока:§c %player%",
            "§c§l[!] §fНаиграно за день:§c %online_day%",
            "§c§l[!] §fНаиграно за неделю:§c %online_week%",
            "§c§l[!] §fНаиграно за месяц:§c %online_month%",
            "§c§l[!] §fВсего наиграли:§c %online_total%",
            "§c§l[!] §fПоследний вход:§c %last_join%",
        });
        msg.setCooldown(new String[] {
            "§c§l[!] §fЧтобы использовать команду подождите§c %cooldown% секунд.",
        });
        msg.setReload(new String[] {
            "§c§l[!] §fКонфигурация и сообщения успешно перезагружены.",
        });
        msg.setNoPermissions(new String[] {
            "§c§l[!] §fУ вас не хватает прав.",
        });
        msg.setPlayerNotFound(new String[] {
            "§c§l[!] §fДосье на §a%player%§f не найдено!",
        });
        return msg;
    }

    public String[] getPersonal() {
        return personal;
    }

    public void setPersonal(String[] personal) {
        this.personal = personal;
    }

    public String[] getAbout() {
        return about;
    }

    public void setAbout(String[] about) {
        this.about = about;
    }

    public String[] getCooldown() {
        return cooldown;
    }

    public void setCooldown(String[] cooldown) {
        this.cooldown = cooldown;
    }

    public String[] getReload() {
        return reload;
    }

    public void setReload(String[] reload) {
        this.reload = reload;
    }

    public String[] getNoPermissions() {
        return noPermissions;
    }

    public void setNoPermissions(String[] noPermissions) {
        this.noPermissions = noPermissions;
    }

    public String[] getPlayerNotFound() {
        return playerNotFound;
    }

    public void setPlayerNotFound(String[] playerNotFound) {
        this.playerNotFound = playerNotFound;
    }

}
