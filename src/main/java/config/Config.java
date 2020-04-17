package main.java.config;

/**
 * Represents config data
 * @author Orachigami
 */
public class Config {
    private Command command;
    private Database database;
    private Options options;

    public Database getDatabase() {
        return database;
    }
    
    public static Config generateDefault() {
        Config config = new Config();
        config.setCommand(Command.generateDefault());
        config.setDatabase(Database.generateDefault());
        config.setOptions(Options.generateDefault());
        return config;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
    

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }
}
