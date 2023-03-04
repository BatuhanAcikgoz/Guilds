package net.devscape.project.guilds.commands;

import net.devscape.project.guilds.Guilds;
import net.devscape.project.guilds.SubCommand;
import net.devscape.project.guilds.handlers.GPlayer;
import net.devscape.project.guilds.handlers.Guild;
import net.devscape.project.guilds.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class Reload extends SubCommand {

    public Reload(final Guilds plugin, final CommandSender sender, final String[] args) {
        super(plugin, sender, args);
        this.execute();
    }

    public void execute() {
        final Player player = (Player) this.getSender();
        if (player.hasPermission("guilds.admin")) {
            Message.send(this.getPlugin(), this.getSender(), "reload");
            Guilds.getInstance().reloadConfig();
            Guilds.getInstance().getData().saveAllData();
        } else {
            Message.send(this.getPlugin(), this.getSender(), "no-permission");
        }
    }
}
