package net.devscape.project.guilds.commands;

import java.util.Optional;
import net.devscape.project.guilds.handlers.Guild;
import net.devscape.project.guilds.util.Message;
import org.bukkit.entity.Player;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandSender;
import net.devscape.project.guilds.Guilds;
import net.devscape.project.guilds.SubCommand;

public class Confirmation extends SubCommand {

    public Confirmation(final Guilds plugin, final CommandSender sender, final String[] args) {
        super(plugin, sender, args);
        this.execute();
    }
    
    private void execute() {
        if (this.getSender() instanceof ConsoleCommandSender) {
            return;
        }
        final Player player = (Player)this.getSender();
        if (!this.getPlugin().getCache().hasPendingTransaction(player.getUniqueId())) {
            return;
        }
        final Optional<Guild> guild = this.getPlugin().getData().getGuild(player.getUniqueId());
        if (!guild.isPresent()) {
            Message.send(this.getPlugin(), this.getSender(), "no-confirmation");
            return;
        }
        this.getPlugin().getData().saveGuild(guild.get());
    }
}
