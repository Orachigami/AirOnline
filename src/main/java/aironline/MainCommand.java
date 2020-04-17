package main.java.aironline;

import main.java.config.Messages;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import java.lang.ref.WeakReference;
import net.md_5.bungee.api.connection.ProxiedPlayer;
/**
 * Main command executor
 * @author Orachigami
 */
public class MainCommand extends Command {
    private Plugin plugin;
    private DatabaseManager manager;
    private String[] aliases;
    private PlayerDataHashMap players;
    private int cooldown;
    
    public MainCommand (Plugin plugin, main.java.config.Command options, DatabaseManager manager, PlayerDataHashMap players) {
        super("aironline");
        this.aliases = options.getAliases();
        this.plugin = plugin;
        this.manager = manager;
        this.players = players;
        this.cooldown = options.getCooldown();
    }
    
    private void sendMessages(CommandSender sender, String[] messages) {
        for (String msg : messages) {
            sender.sendMessage(TextComponent.fromLegacyText(msg));
        }
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        boolean isAdmin = sender.hasPermission("aironline.admin");
        boolean isUser = isAdmin | sender.hasPermission("aironline.personal");
        StringBuilder[] prepared_messages;
        WeakReference<StringBuilder[]> ref;
        WeakReference<String[]> ref2;
        String[] messages;
        String player_name = null;
        PlayerData player_data = null;
        if (!isUser) {
            messages = AirOnline.messages.getNoPermissions();
            sendMessages(sender, messages);
            return;
        }
        if (args.length == 0) {
            player_name = sender.getName();
            
            messages = AirOnline.messages.getPersonal();
        } else {
            String arg = args[0];
            switch (arg) {
                /*case "help":
                    sender.sendMessage(TextComponent.fromLegacyText("Help")); 
                    messages = null;
                    break;*/
                case "reload": 
                    if (!isAdmin) {
                        messages = AirOnline.messages.getNoPermissions();
                        sendMessages(sender, messages);
                        return;
                    }
                    plugin.getProxy().getPluginManager().unregisterCommand(this);
                    plugin.onEnable();
                    messages = AirOnline.messages.getReload();
                    sendMessages(sender, messages);
                    prepared_messages = null;
                    messages = null;
                    return;
                default: 
                    if (!isAdmin) {
                        messages = AirOnline.messages.getNoPermissions();
                        sendMessages(sender, messages);
                        return;
                    }
                    player_name = arg;
                    messages = AirOnline.messages.getAbout();
                    break;
            }
        }
        ProxiedPlayer player = plugin.getProxy().getPlayer(sender.getName());
        if (player != null) {
            PlayerData known_data = players.get(player.getUniqueId());
            if (!isAdmin && known_data != null) {
                long left = known_data.getCooldown() - System.currentTimeMillis();
                if (left > 0) {
                    messages = AirOnline.messages.getCooldown();
                    prepared_messages = new StringBuilder[messages.length];
                    ref = new WeakReference(prepared_messages);
                    for (int i = 0; i < messages.length; i++) {
                        prepared_messages[i] = new StringBuilder(messages[i]);
                        Messages.substitute(prepared_messages[i], Messages.COOLDOWN, Long.toString(left / 1000L));
                    }
                    messages = new String[messages.length];
                    ref2 = new WeakReference(messages);
                    for (int i = 0; i < messages.length; i++) {
                        messages[i] = prepared_messages[i].toString();
                    }
                    sendMessages(sender, messages);
                    return;
                } else {
                    known_data.setCooldown(System.currentTimeMillis() + cooldown * 1000L);
                }
            }
        }
        //else { return; }
        player_data = manager.getData(player_name);
        if (player_data == null) {
            messages = AirOnline.messages.getPlayerNotFound();
            prepared_messages = new StringBuilder[messages.length];
            ref = new WeakReference(prepared_messages);
            for (int i = 0; i < messages.length; i++) {
                prepared_messages[i] = new StringBuilder(messages[i]);
                Messages.substitute(prepared_messages[i], Messages.PLAYER, player_name);
            }
            messages = new String[messages.length];
            ref2 = new WeakReference(messages);
            for (int i = 0; i < messages.length; i++) {
                messages[i] = prepared_messages[i].toString();
            }
            sendMessages(sender, messages);
            prepared_messages = null;
            messages = null;
            return;
        }
        prepared_messages = new StringBuilder[messages.length];
        ref = new WeakReference(prepared_messages);

        for (int i = 0; i < messages.length; i++) {
            prepared_messages[i] = new StringBuilder(messages[i]);
            Messages.substitute(prepared_messages[i], Messages.PLAYER, player_name);
            Messages.substitute(prepared_messages[i], Messages.ONLINE_DAY, String.format("%.2f", player_data.getDayPlayedTime() / 3600000.0));
            Messages.substitute(prepared_messages[i], Messages.ONLINE_WEEK, String.format("%.2f", player_data.getWeekPlayedTime() / 3600000.0));
            Messages.substitute(prepared_messages[i], Messages.ONLINE_MONTH, String.format("%.2f", player_data.getMonthPlayedTime() / 3600000.0));
            Messages.substitute(prepared_messages[i], Messages.ONLINE_TOTAL, String.format("%.2f", player_data.getTotalPlayedTime() / 3600000.0));

            Date date = Date.from(Instant.ofEpochMilli(player_data.getLastPlayedTime()));
            String format = new SimpleDateFormat("d.MM.y H:m").format(date);
            Messages.substitute(prepared_messages[i], Messages.LAST_JOIN, format);
        }
        messages = new String[messages.length];
        ref2 = new WeakReference(messages);
        for (int i = 0; i < messages.length; i++) {
            messages[i] = prepared_messages[i].toString();
        }
        sendMessages(sender, messages);
        prepared_messages = null;
        messages = null;
    }
    
    @Override
    public String[] getAliases() {
        return aliases;
    }
}