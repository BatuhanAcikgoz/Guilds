package net.devscape.project.guilds.commands;

import java.util.Optional;
import net.devscape.project.guilds.util.Message;
import net.devscape.project.guilds.handlers.GPlayer;
import net.devscape.project.guilds.handlers.Role;
import net.devscape.project.guilds.handlers.Guild;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import net.devscape.project.guilds.Guilds;
import net.devscape.project.guilds.SubCommand;

public class CreateGuild extends SubCommand {

    public CreateGuild(final Guilds plugin, final CommandSender sender, final String[] args) {
        super(plugin, sender, args);
        this.execute();
    }
    
    public void execute() {
        final Player player = (Player)this.getSender();
        final String name = this.getArgs()[1];
        final Optional<Guild> guild = this.getPlugin().getData().getGuild(name);
        final Optional<GPlayer> gPlayer = this.getPlugin().getData().getPlayer(player.getUniqueId());
        if (!guild.isPresent()) {
            if (!gPlayer.isPresent()) {
                if (this.getPlugin().getConfig().getBoolean("settings.enable-guild-creation-cost")) {
                    if (Guilds.getEcon().has(Bukkit.getOfflinePlayer(player.getUniqueId()), this.getPlugin().getConfig().getDouble("settings.guild-creation-cost"))) {
                        Guilds.getEcon().withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), this.getPlugin().getConfig().getDouble("settings.guild-creation-cost"));
                        this.getPlugin().getData().saveGuild(new Guild(player.getUniqueId(), name));
                        this.getPlugin().getData().savePlayer(new GPlayer(player.getUniqueId(), name, Role.LEADER));
                        Message.send(this.getPlugin(), (CommandSender)player, "create-guild");
                    }
                    else {
                        Message.sendPlaceholder(this.getPlugin(), this.getSender(), "guild-create-insufficient-funds", String.valueOf(this.getPlugin().getConfig().getDouble("settings.guild-creation-cost")));
                    }
                }
                else {
                    this.getPlugin().getData().saveGuild(new Guild(player.getUniqueId(), name));
                    this.getPlugin().getData().savePlayer(new GPlayer(player.getUniqueId(), name, Role.LEADER));
                    Message.send(this.getPlugin(), (CommandSender)player, "create-guild");
                }
            }
            else {
                Message.send(this.getPlugin(), this.getSender(), "already-in-guild");
            }
        }
        else {
            Message.send(this.getPlugin(), this.getSender(), "guild-already-exists");
        }
    }
}
