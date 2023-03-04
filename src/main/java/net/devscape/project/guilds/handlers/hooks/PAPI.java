package net.devscape.project.guilds.handlers.hooks;

import java.util.Optional;
import org.bukkit.Bukkit;
import net.devscape.project.guilds.handlers.Guild;
import org.bukkit.OfflinePlayer;
import net.devscape.project.guilds.Guilds;
import org.jetbrains.annotations.NotNull;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PAPI extends PlaceholderExpansion {

    @NotNull
    public String getIdentifier() {
        return "guilds";
    }
    
    @NotNull
    public String getAuthor() {
        return "DevScape";
    }
    
    @NotNull
    public String getVersion() {
        return Guilds.getInstance().getDescription().getVersion();
    }
    
    public boolean persist() {
        return true;
    }
    
    public String onRequest(final OfflinePlayer player, final String params) {
        if (params.equalsIgnoreCase("name")) {
            final Optional<Guild> guild = Guilds.getInstance().getData().getGuild(player.getUniqueId());
            String text;
            if (guild.isPresent()) {
                text = guild.get().getName();
            }
            else {
                text = "";
            }
            return text;
        }
        if (params.equalsIgnoreCase("leader")) {
            final Optional<Guild> guild = Guilds.getInstance().getData().getGuild(player.getUniqueId());
            String text;
            if (guild.isPresent()) {
                text = Bukkit.getOfflinePlayer(guild.get().getOwner()).getName();
            }
            else {
                text = "";
            }
            return text;
        }
        if (params.equalsIgnoreCase("description")) {
            final Optional<Guild> guild = Guilds.getInstance().getData().getGuild(player.getUniqueId());
            String text;
            if (guild.isPresent()) {
                text = guild.get().getDescription();
            }
            else {
                text = "";
            }
            return text;
        }
        if (params.equalsIgnoreCase("members_count")) {
            final Optional<Guild> guild = Guilds.getInstance().getData().getGuild(player.getUniqueId());
            int value;
            if (guild.isPresent()) {
                value = guild.get().getMembers().size();
            }
            else {
                value = 0;
            }
            return String.valueOf(value);
        }
        if (params.equalsIgnoreCase("level")) {
            final Optional<Guild> guild = Guilds.getInstance().getData().getGuild(player.getUniqueId());
            int value;
            if (guild.isPresent()) {
                value = guild.get().getLevel();
            }
            else {
                value = 0;
            }
            return String.valueOf(value);
        }
        return null;
    }
}
