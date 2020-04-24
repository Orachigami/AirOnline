package main.java.aironline;

import java.sql.*;
import java.util.Collection;
import java.util.UUID;
import main.java.config.Database;

/**
 * Class which manages database operations
 * @author Orachigami
 */
public final class DatabaseManager {
    
    /** Singleton DatabaseManager instance */
    private static DatabaseManager instance = null;
    
    /** JDBC connection link */
    private static String connectionUrl = null;
    
    private String tableName = null;
    
    private DatabaseManager() {}
    
    /**
     * Generates Singleton DatabaseManager
     * @param host IP address for database host
     * @param name Database Name
     * @param user Database Username
     * @param password Database Password
     * @throws ClassNotFoundException If JDBC falls
     * @throws InstantiationException If JDBC falls
     * @throws IllegalAccessException If JDBC falls 
     */
    private DatabaseManager(String host, int port, String name, String user, String password, String tableName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        connectionUrl = String.format("jdbc:mysql://%s:%d/%s?allowPublicKeyRetrieval=true&useSSL=false&user=%s&password=%s&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC", host, port, name, user,  password);
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        this.tableName = tableName;
    }
    
    /**
     * Generates Singleton DatabaseManager
     * @param config Database config
     * @return DatabaseManager instance
     * @throws ClassNotFoundException If JDBC falls
     * @throws InstantiationException If JDBC falls
     * @throws IllegalAccessException If JDBC falls 
     */
    public static DatabaseManager createInstance(Database config) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (instance == null) {
            instance = new DatabaseManager(config.getHost(), config.getPort(), config.getName(), config.getUsername(), config.getPassword(), config.getTable());
        }
        return instance;
    }
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionUrl);
    }
    
    /**
     * Creates database table if none exist
     */
    public void init() {
        try (Connection connection = getConnection()) {
            Statement st = connection.createStatement();
            st.execute(String.format("CREATE TABLE IF NOT EXISTS `%s` (`uuid` CHAR(36) NOT NULL PRIMARY KEY, `name` VARCHAR(50) NOT NULL, `dayPlayed` BIGINT NOT NULL DEFAULT 0, `weekPlayed` BIGINT NOT NULL DEFAULT 0, `monthPlayed` BIGINT NOT NULL DEFAULT 0, `totalPlayed` BIGINT NOT NULL DEFAULT 0, `lastPlayed` BIGINT NOT NULL)", tableName));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean updateData(Collection<PlayerData> list) {
        if (!list.isEmpty()) {
            try (Connection connection = getConnection()) {
                PreparedStatement pst = connection.prepareStatement(String.format("INSERT INTO `%s` (uuid, name, dayPlayed, weekPlayed, monthPlayed, totalPlayed, lastPlayed) VALUES(?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE dayPlayed = dayPlayed + VALUES(dayPlayed), weekPlayed = weekPlayed + VALUES(weekPlayed), monthPlayed = monthPlayed + VALUES(monthPlayed), totalPlayed = totalPlayed + VALUES(totalPlayed), lastPlayed = VALUES(lastPlayed)", tableName));
                for (PlayerData playerData : list) {
                    pst.setString(1, playerData.getUUID().toString());
                    pst.setString(2, playerData.getName());
                    pst.setLong(3, playerData.getTotalPlayedTime());
                    pst.setLong(4, playerData.getTotalPlayedTime());
                    pst.setLong(5, playerData.getTotalPlayedTime());
                    pst.setLong(6, playerData.getTotalPlayedTime());
                    pst.setLong(7, playerData.getLastPlayedTime());
                    pst.addBatch();
                }
                pst.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    public PlayerData getData(String name) {
        try (Connection connection = getConnection()) {
            PreparedStatement pst = connection.prepareStatement(String.format("SELECT * FROM `%s` WHERE name = ? LIMIT 1", tableName));
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new PlayerData(UUID.fromString(rs.getString("uuid")), name, rs.getLong("lastPlayed"), rs.getLong("dayPlayed"), rs.getLong("weekPlayed"), rs.getLong("monthPlayed"), rs.getLong("totalPlayed"), false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void resetDay() {
        try (Connection connection = getConnection()) {
            PreparedStatement pst = connection.prepareStatement(String.format("UPDATE `%s` SET dayPlayed = 0;", tableName));
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void resetWeek() {
        try (Connection connection = getConnection()) {
            PreparedStatement pst = connection.prepareStatement(String.format("UPDATE `%s` SET dayPlayed = 0, weekPlayed = 0;", tableName));
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void resetMonth() {
        try (Connection connection = getConnection()) {
            PreparedStatement pst = connection.prepareStatement(String.format("UPDATE `%s` SET dayPlayed = 0, weekPlayed = 0, monthPlayed = 0;", tableName));
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
