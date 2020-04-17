package main.java.config;

/**
 * Represents configuration for command executing
 * @author Orachigami
 */
public class Command {
    private int cooldown;
    private String[] aliases;
    
    /**
     * Returns Command with default configuration
     */
    public static Command generateDefault() {
       Command cmd = new Command();
       cmd.setCooldown(30);
       cmd.setAliases(new String[] {"aon"});
       return cmd;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }
}
