package main.java.config;

/**
 * Represents configuration for MySQL database management
 * @author Orachigami
 */
public class Database {
    private String host;
    private int port;
    private String name;
    private String username;
    private String password;
    private String table;
    private int updateTime;
    
    public static Database generateDefault() {
        Database db = new Database();
        db.setHost("127.0.0.1");
        db.setPort(3306);
        db.setName("freshworld_main");
        db.setUsername("aslanikoko123");
        db.setPassword("aslanikoko123");
        db.setTable("AirOnlinePlugin");
        db.setUpdateTime(10);
        return db;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }
    
}
