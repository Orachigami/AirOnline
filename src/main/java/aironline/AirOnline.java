package main.java.aironline;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import main.java.config.Config;
import main.java.config.Database;
import main.java.config.Messages;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Plugin for players online monitoring
 * @author Orachigami
 * @TODO Connection Pool
 */
public class AirOnline extends Plugin {
    
    private PlayerEventListener listener;
    private DatabaseManager manager;
    private Config config;
    private ScheduledTask task;
    public static Messages messages;
    public PlayerDataHashMap players;
    private LocalDateTime endOfDay;
    private LocalDateTime endOfWeek;
    int weekNumber;
    private LocalDateTime endOfMonth;
    
    /**
     * Creates default config and messages files if none of them found
     */
    private void saveDefaultConfig() {
        getDataFolder().mkdir();
        File config_file = new File(getDataFolder(), "config.json");
        File msg_file = new File(getDataFolder(), "messages.json");
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter fw;
            if (config_file.createNewFile()) {
                fw = new FileWriter(config_file);
                fw.append(gson.toJson(Config.generateDefault()));
                fw.close();
            }
            if (msg_file.createNewFile()) {
                fw = new FileWriter(msg_file);
                fw.append(gson.toJson(Messages.generateDefault()));
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Loads config and messages files
     */
    private void loadConfig() {
        File config_file = new File(getDataFolder(), "config.json");
        File msg_file = new File(getDataFolder(), "messages.json");
        Gson gson = new Gson();
        try {
            FileReader fr = new FileReader(config_file);
            this.config = gson.fromJson(fr, Config.class);
            fr.close();
            fr = new FileReader(msg_file);
            messages = gson.fromJson(fr, Messages.class);
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Initilizates DatabaseManager
     */
    private void initDatabase() {
        Database database = config.getDatabase();
        String host = database.getHost();
        int port = database.getPort();
        String name = database.getName();
        String user = database.getUsername();
        String password = database.getPassword();
        try {
            manager = DatabaseManager.createInstance(host, port, name, user, password);
            manager.init();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Init Players event listener and schedule data update
     */
    private void initEventListener() {
        if (task != null) task.cancel();
        listener = new PlayerEventListener(config.getOptions().getIgnoredServers(), players);
        getProxy().getPluginManager().registerListener(this, listener);
        long period = 10L;
        TaskScheduler scheduler = getProxy().getScheduler();
        task = scheduler.schedule(this, () -> {
            LocalDateTime now = LocalDateTime.now();
            
            if (endOfDay.isBefore(now)) {
                if (endOfWeek.isBefore(now)) {
                    if (endOfMonth.isBefore(now)) {
                        manager.resetMonth();
                        endOfDay = LocalDate.now().atTime(LocalTime.MIN).plusDays(1);
                        endOfWeek = LocalDate.now().atTime(LocalTime.MIN).withDayOfMonth(7);
                        endOfMonth = LocalDate.now().atTime(LocalTime.MIN).withDayOfMonth(1).plusMonths(1);
                        weekNumber = 0;
                    } else {
                        manager.resetWeek();
                        endOfDay = LocalDate.now().atTime(LocalTime.MIN).plusDays(1);
                        if (weekNumber++ == 3) {
                            endOfWeek = LocalDate.now().atTime(LocalTime.MIN).withDayOfMonth(1).plusMonths(1);
                            weekNumber = 0;
                        } else {
                            endOfWeek = LocalDate.now().atTime(LocalTime.MIN).plusDays(7);
                        }
                    }
                } else {
                    manager.resetDay();
                    endOfDay = LocalDate.now().atTime(LocalTime.MIN).plusDays(1);
                }
            }
            manager.updateData(players.getValues());
            players.resetAll();
        }, period, period, TimeUnit.MINUTES);
    }
    
    @Override
    public void onEnable() {
        LocalDate localDate = LocalDate.now();
        int weekday = (localDate.getDayOfMonth() / 7) * 7 + 7;
        endOfDay = localDate.atTime(LocalTime.MIN).plusDays(1);
        if (localDate.getDayOfMonth() > 27) {
            endOfWeek = localDate.atTime(LocalTime.MIN).withDayOfMonth(1).plusMonths(1);
        } else {
            endOfWeek = localDate.atTime(LocalTime.MIN).withDayOfMonth(weekday);
        }
        endOfMonth = localDate.atTime(LocalTime.MIN).withDayOfMonth(1).plusMonths(1);
        weekNumber = 0;
        
        if (players == null) players = new PlayerDataHashMap();
        saveDefaultConfig();
        loadConfig();
        initDatabase();
        initEventListener();
        Command command = new MainCommand(this, config.getCommand(), manager, players);
        getProxy().getPluginManager().registerCommand(this, command);
    }
}
