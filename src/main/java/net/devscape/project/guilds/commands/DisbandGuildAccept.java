package net.devscape.project.guilds.commands;

import java.util.Optional;
import net.devscape.project.guilds.handlers.Guild;
import net.devscape.project.guilds.util.Message;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import net.devscape.project.guilds.Guilds;
import net.devscape.project.guilds.SubCommand;

public class DisbandGuildAccept extends SubCommand {

    public DisbandGuildAccept(final Guilds plugin, final CommandSender sender, final String[] args) {
        super(plugin, sender, args);
        this.execute();
    }
    
    public boolean execute() {
        final Player player = (Player)this.getSender();
        try {
            final Optional<Guild> guild = this.getPlugin().getData().getGuild(player.getUniqueId());
            if (!guild.isPresent()) {
                Message.send(this.getPlugin(), this.getSender(), "must-be-owner");
                return true;
            }
            if (guild.get().getOwner().equals(player.getUniqueId())) {

                this.getPlugin().getData().deleteGuild(guild.get().getName());
                Message.send(this.getPlugin(), this.getSender(), "disband-guild");
            }
            else {
                Message.send(this.getPlugin(), this.getSender(), "must-be-owner");
            }
        }
        catch (NullPointerException e) {
            return false;
        }
        return true;
    }
}
