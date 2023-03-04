package net.devscape.project.guilds.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import net.devscape.project.guilds.handlers.Guild;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.ChatColor;
import java.util.Objects;
import org.bukkit.command.CommandSender;
import net.devscape.project.guilds.Guilds;

public class Message {

    public static void send(final Guilds plugin, final CommandSender sender, final String messagePath) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(plugin.getConfig().getString("messages." + messagePath))));
    }
    
    public static void sendPlaceholder(final Guilds plugin, final CommandSender sender, final String messagePath, final String placeholder) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages." + messagePath)).replace("%placeholder%", placeholder)));
    }
    
    public static void sendPlaceholders(final Guilds plugin, final CommandSender sender, final String messagePath, final Map<String, String> placeholders) {
        String output = plugin.getConfig().getString("messages." + messagePath);
        for (final String placeholder : placeholders.keySet()) {
            output = Objects.requireNonNull(output).replace(placeholder, placeholders.get(placeholder));
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(output)));
    }
    
    public static void sendPlaceholderPath(final Guilds plugin, final CommandSender sender, final String messagePath, final String placeholder) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString(messagePath)).replace("%placeholder%", placeholder)));
    }
    
    public static void sendPlaceholder(final Guilds plugin, final Player sender, final String messagePath, final String placeholder) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages." + messagePath)).replace("%placeholder%", placeholder)));
    }
    
    public static void sendHelp(final Guilds plugin, final CommandSender sender, final String messagePath, final String placeholder) {
        final ConfigurationSection section = plugin.getConfig().getConfigurationSection(messagePath);
        for (final String key : Objects.requireNonNull(section).getKeys(false)) {
            if (key.equals("header")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(plugin.getConfig().getString(messagePath + "." + key))));
            }
            else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString(messagePath + "." + key)).replace("%placeholder%", placeholder)));
            }
        }
    }
    
    public static void sendGuildList(final Guilds plugin, final CommandSender sender, final String messagePath, final String[] placeholder) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages." + messagePath)).replace("%number%", placeholder[0]).replace("%guildName%", placeholder[1]).replace("%members%", placeholder[2])));
    }
    
    public static void sendGuildInfo(final Guilds plugin, final CommandSender sender, final String messagePath, final Guild guild) {
        final ConfigurationSection section = plugin.getConfig().getConfigurationSection(messagePath);
        for (final String s : Objects.requireNonNull(section).getKeys(false)) {
            final String key = s;
            switch (s) {
                case "header": {
                    sendPlaceholderPath(plugin, sender, messagePath + "." + key, guild.getName());
                    continue;
                }
                case "leader": {
                    sendPlaceholderPath(plugin, sender, messagePath + "." + key, Bukkit.getOfflinePlayer(guild.getOwner()).getName());
                    continue;
                }
                case "description": {
                    sendPlaceholderPath(plugin, sender, messagePath + "." + key, guild.getDescription());
                    continue;
                }
                case "level": {
                    sendPlaceholderPath(plugin, sender, messagePath + "." + key, guild.getLevel() + "/" + guild.getMaxLevel());
                    continue;
                }
                case "size": {
                    sendPlaceholderPath(plugin, sender, messagePath + "." + key, guild.getMembers().size() + "/" + guild.getMaxMembers());
                    continue;
                }
                case "online": {
                    int online = 0;
                    for (final UUID member : guild.getMembers().keySet()) {
                        if (Bukkit.getPlayer(member) != null) {
                            ++online;
                        }
                    }
                    sendPlaceholderPath(plugin, sender, messagePath + "." + key, online + "/" + guild.getMembers().size());
                    continue;
                }
                case "members": {
                    final StringBuilder string = new StringBuilder();
                    string.append(plugin.getConfig().getString(messagePath + "." + key)).append(" ");
                    for (final UUID member2 : guild.getMembers().keySet()) {
                        string.append(ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(section.getString("member-color")))).append(Bukkit.getOfflinePlayer(member2).getName()).append(" ");
                    }
                    sendRaw(sender, string.toString());
                    continue;
                }
                case "bottom": {
                    send(plugin, sender, "guild-info." + key);
                    continue;
                }
            }
        }
    }
    
    public static void sendRaw(final CommandSender sender, final String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
    
    public static void sendMessage(final Player p, final String msg) {
        p.sendMessage(format(msg));
    }
    
    public static String deformat(String str) {
        return ChatColor.stripColor(format(str));
    }
    
    public static List<String> format(final List<String> lore) {
        return lore.stream().map(Message::format).collect(Collectors.toList());
    }
    
    public static String format(String message) {
        message = message.replace(">>", "").replace("<<", "");
        final Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]){6}");
        for (Matcher matcher = hexPattern.matcher(message); matcher.find(); matcher = hexPattern.matcher(message)) {
            final net.md_5.bungee.api.ChatColor hexColor = net.md_5.bungee.api.ChatColor.of(matcher.group().substring(1));
            final String before = message.substring(0, matcher.start());
            final String after = message.substring(matcher.end());
            message = before + hexColor + after;
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
