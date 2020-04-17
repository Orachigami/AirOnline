package main.java.aironline;

import java.sql.*;
import java.util.Collection;
import java.util.UUID;

/**
 * Class which manages database operations
 * @author Orachigami
 */
public final class DatabaseManager {
    
    /** Singleton DatabaseManager instance */
    private static DatabaseManager instance = null;
    
    /** JDBC connection link */
    private static String connectionUrl = null;
    
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
    private DatabaseManager(String host, int port, String name, String user, String password) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        connectionUrl = String.format("jdbc:mysql://%s:%d/%s?allowPublicKeyRetrieval=true&useSSL=false&user=%s&password=%s&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC", host, port, name, user,  password);
        Class.forName("com.mysql.jdbc.Driver").newInstance();
    }
    
    /**
     * Generates Singleton DatabaseManager
     * @param host IP address for database host
     * @param port Database port
     * @param name Database Name
     * @param user Database Username
     * @param password Database Password
     * @throws ClassNotFoundException If JDBC falls
     * @throws InstantiationException If JDBC falls
     * @throws IllegalAccessException If JDBC falls 
     */
    public static DatabaseManager createInstance(String host, int port, String name, String user, String password) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (instance == null) {
            instance = new DatabaseManager(host, port, name, user, password);
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
            st.execute("CREATE TABLE IF NOT EXISTS `AirOnlinePlugin` (`uuid` CHAR(36) NOT NULL PRIMARY KEY, `name` VARCHAR(50) NOT NULL, `dayPlayed` BIGINT NOT NULL DEFAULT 0, `weekPlayed` BIGINT NOT NULL DEFAULT 0, `monthPlayed` BIGINT NOT NULL DEFAULT 0, `totalPlayed` BIGINT NOT NULL DEFAULT 0, `lastPlayed` BIGINT NOT NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean updateData(Collection<PlayerData> list) {
        try (Connection connection = getConnection()) {
            PreparedStatement pst = connection.prepareStatement("INSERT INTO `AirOnlinePlugin` (uuid, name, dayPlayed, weekPlayed, monthPlayed, totalPlayed, lastPlayed) VALUES(?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE dayPlayed = VALUES(dayPlayed) + dayPlayed, weekPlayed = VALUES(weekPlayed) + weekPlayed, monthPlayed = VALUES(monthPlayed) + monthPlayed, totalPlayed = VALUES(totalPlayed) + totalPlayed, lastPlayed = VALUES(lastPlayed)");
            for (PlayerData playerData : list) {
                pst.setString(1, playerData.getUUID().toString());
                pst.setString(2, playerData.getName());
                pst.setLong(3, playerData.getDayPlayedTime());
                pst.setLong(4, playerData.getWeekPlayedTime());
                pst.setLong(5, playerData.getMonthPlayedTime());
                pst.setLong(6, playerData.getTotalPlayedTime());
                pst.setLong(7, playerData.getLastPlayedTime());
                pst.addBatch();
            }
            pst.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public PlayerData getData(String name) {
        try (Connection connection = getConnection()) {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM AirOnlinePlugin WHERE name = ? LIMIT 1");
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
            PreparedStatement pst = connection.prepareStatement("UPDATE AirOnlinePlugin SET dayPlayed = 0;");
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void resetWeek() {
        try (Connection connection = getConnection()) {
            PreparedStatement pst = connection.prepareStatement("UPDATE AirOnlinePlugin SET dayPlayed = 0, weekPlayed = 0;");
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void resetMonth() {
        try (Connection connection = getConnection()) {
            PreparedStatement pst = connection.prepareStatement("UPDATE AirOnlinePlugin SET dayPlayed = 0, weekPlayed = 0, monthPlayed = 0;");
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
