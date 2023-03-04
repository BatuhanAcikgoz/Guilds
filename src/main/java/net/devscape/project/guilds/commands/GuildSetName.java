package net.devscape.project.guilds.commands;

import net.devscape.project.guilds.Guilds;
import net.devscape.project.guilds.SubCommand;
import net.devscape.project.guilds.handlers.GPlayer;
import net.devscape.project.guilds.handlers.Guild;
import net.devscape.project.guilds.util.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class GuildSetName extends SubCommand {
    public GuildSetName(final Guilds plugin, final CommandSender sender, final String[] args) {
        super(plugin, sender, args);
        this.execute();
    }
    
    private void execute() {
        final Player player = (Player)this.getSender();
        final Optional<Guild> guild = this.getPlugin().getData().getGuild(player.getUniqueId());
        try {
            if (!guild.isPresent()) {
                Message.send(this.getPlugin(), this.getSender(), "must-be-owner");
            }
            if (guild.get().getOwner().equals(player.getUniqueId())) {

                final StringBuilder name = new StringBuilder();
                for (final String arg : this.getArgs()) {
                    if (!arg.equals("setname")) {
                        name.append(arg).append(" ");
                    }
                }
                this.getPlugin().getData().GuildSetNameSQL(player.getUniqueId(), name.toString());
                Message.sendPlaceholder(this.getPlugin(), this.getSender(), "set-name", name.toString());
            }
            else {
                Message.send(this.getPlugin(), this.getSender(), "must-be-owner");
            }
        }
        catch (NullPointerException e) {
        }
    }
}
