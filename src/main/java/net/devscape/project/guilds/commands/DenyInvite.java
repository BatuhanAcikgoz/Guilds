package net.devscape.project.guilds.commands;

import net.devscape.project.guilds.util.Message;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import net.devscape.project.guilds.Guilds;
import net.devscape.project.guilds.SubCommand;

public class DenyInvite extends SubCommand {

    public DenyInvite(final Guilds plugin, final CommandSender sender, final String[] args) {
        super(plugin, sender, args);
        this.execute();
    }
    
    private void execute() {
        if (this.getSender() instanceof Player) {
            final Player player = (Player)this.getSender();
            final String name = this.getArgs()[1];
            try {
                if (!this.getPlugin().getData().hasInvite(player.getUniqueId(), name)) {
                    Message.sendPlaceholder(this.getPlugin(), this.getSender(), "no-invite", name);
                }
                else {
                    this.getPlugin().getData().deleteInvite(player.getUniqueId(), name);
                    Message.sendPlaceholder(this.getPlugin(), this.getSender(), "invite-denied", name);
                }
            }
            catch (NullPointerException e) {
                Message.sendPlaceholder(this.getPlugin(), this.getSender(), "no-invite", name);
            }
        }
        else {
            Message.send(this.getPlugin(), this.getSender(), "not-console-command");
        }
    }
}
