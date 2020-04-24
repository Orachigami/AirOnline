package main.java.config;

/**
 * Represents additional plugin options
 * @author Orachigami
 */
public class Options {
    private boolean enableIgnoredServers;
    private String[] ignoredServers;
    
    public static Options generateDefault() {
        Options options = new Options();
        options.setEnableIgnoredServers(false);
        options.setIgnoredServers(new String[] {"auth"});
        return options;
    }

    public String[] getIgnoredServers() {
        return ignoredServers;
    }

    public void setIgnoredServers(String[] ignoredServers) {
        this.ignoredServers = ignoredServers;
    }

    public boolean isEnableIgnoredServers() {
        return enableIgnoredServers;
    }

    public void setEnableIgnoredServers(boolean enableIgnoredServers) {
        this.enableIgnoredServers = enableIgnoredServers;
    }
}
