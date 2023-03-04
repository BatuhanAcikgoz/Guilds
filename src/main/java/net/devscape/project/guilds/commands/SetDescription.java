package net.devscape.project.guilds.commands;

import java.util.Optional;
import net.devscape.project.guilds.handlers.Guild;
import net.devscape.project.guilds.util.Message;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import net.devscape.project.guilds.Guilds;
import net.devscape.project.guilds.SubCommand;

public class SetDescription extends SubCommand {
    public SetDescription(final Guilds plugin, final CommandSender sender, final String[] args) {
        super(plugin, sender, args);
        this.execute();
    }

    private void execute() {
        final Player player = (Player)this.getSender();
        final Optional<Guild> guild = this.getPlugin().getData().getGuild(player.getUniqueId());
        if (!guild.isPresent()) {
            Message.send(this.getPlugin(), this.getSender(), "generic-error");
            return;
        }
        try {
            final StringBuilder desc = new StringBuilder();
            for (final String arg : this.getArgs()) {
                if (!arg.equals("setdesc")) {
                    desc.append(arg).append(" ");
                }
            }
            guild.get().setDescription(desc.toString().trim());
            this.getPlugin().getData().saveGuild(guild.get());
            Message.sendPlaceholder(this.getPlugin(), this.getSender(), "set-description", desc.toString());
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            Message.send(this.getPlugin(), this.getSender(), "generic-error");
        }
    }
}
