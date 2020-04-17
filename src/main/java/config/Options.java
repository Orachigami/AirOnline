package main.java.config;

/**
 * Represents additional plugin options
 * @author Orachigami
 */
public class Options {
    private String[] ignoredServers;
    
    public static Options generateDefault() {
        Options options = new Options();
        options.setIgnoredServers(new String[] {"auth"});
        return options;
    }

    public String[] getIgnoredServers() {
        return ignoredServers;
    }

    public void setIgnoredServers(String[] ignoredServers) {
        this.ignoredServers = ignoredServers;
    }
}
