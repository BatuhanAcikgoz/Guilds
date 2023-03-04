// 
// Decompiled by Procyon v0.5.36
// 

package net.devscape.project.guilds.commands;

import java.util.Optional;
import net.devscape.project.guilds.util.Message;
import net.devscape.project.guilds.handlers.GPlayer;
import net.devscape.project.guilds.handlers.Guild;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import net.devscape.project.guilds.Guilds;
import net.devscape.project.guilds.SubCommand;
import org.bukkit.metadata.FixedMetadataValue;

public class Chat extends SubCommand
{
    private Guilds plugin;
    public Chat(final Guilds plugin, final CommandSender sender, final String[] args) {
        super(plugin, sender, args);
        this.execute();
    }
    
    public void execute() {
        final Player player = (Player)this.getSender();
            final Optional<Guild> guild = this.getPlugin().getData().getGuild(player.getUniqueId());
            final Optional<GPlayer> gPlayer = this.getPlugin().getData().getPlayer(player.getUniqueId());
            if (guild.isPresent()) {
                if (gPlayer.isPresent()) {
                            if (player.hasMetadata("RadomeGuildChat")) {
                                player.removeMetadata("RadomeGuildChat", this.getPlugin());
                                Message.send(this.getPlugin(), this.getSender(), "guild-chat-off");
                            } else {
                                player.setMetadata("RadomeGuildChat", new FixedMetadataValue(this.getPlugin(), true));
                                Message.send(this.getPlugin(), this.getSender(), "guild-chat-on");
                            }
                }
                else {
                    Message.send(this.getPlugin(), this.getSender(), "not-in-guild");
                }
            }
            else {
                Message.send(this.getPlugin(), this.getSender(), "not-in-guild");
            }
        }
    }
