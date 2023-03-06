package net.devscape.project.guilds.handlers;

import ga.strikepractice.StrikePractice;
import ga.strikepractice.api.StrikePracticeAPI;
import ga.strikepractice.events.DuelEndEvent;
import ga.strikepractice.fights.Fight;
import net.devscape.project.guilds.Guilds;
import net.devscape.project.guilds.util.Message;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.*;

public class RadomeAPI implements Listener {

    private HashMap<Player, Integer> killStreaks = new HashMap<>();
    private Guilds plugin;
    public RadomeAPI(Guilds plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        Player player = e.getPlayer();
        final Optional<Guild> guild = Guilds.getInstance().getData().getGuild(player.getUniqueId());
        if (guild.isPresent()) {
            player.removeMetadata("RadomeGuildChat", this.plugin);player.removeMetadata("RadomeGuildChat", this.plugin);
        }
    }

    @EventHandler
    public void onJoinCommand(final PlayerJoinEvent e) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        String command = "usersitelink " + e.getPlayer().getUniqueId();
        Bukkit.dispatchCommand(console, command);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        Player player = e.getPlayer();
        final Optional<Guild> guild = Guilds.getInstance().getData().getGuild(player.getUniqueId());
        if (guild.isPresent()) {
            player.setTotalExperience(0);
            player.setLevel(0);
            player.setExp(0);
            player.giveExp(guild.get().getLevelExp());
        }
    }

    @EventHandler
    public void onRespawn(final PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        final Optional<Guild> guild1 = Guilds.getInstance().getData().getGuild(player.getUniqueId());
        if (guild1.isPresent()) {
            player.setTotalExperience(0);
            player.setLevel(0);
            player.setExp(0);
            player.giveExp(guild1.get().getLevelExp());
        } else {
            player.setTotalExperience(0);
            player.setLevel(0);
            player.setExp(0);
        }
    }

    @EventHandler
    public void onWorldChange(final PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();
        final Optional<Guild> guild1 = Guilds.getInstance().getData().getGuild(player.getUniqueId());
        if (guild1.isPresent()) {
            player.setTotalExperience(0);
            player.setLevel(0);
            player.setExp(0);
            player.giveExp(guild1.get().getLevelExp());
        } else {
            player.setTotalExperience(0);
            player.setLevel(0);
            player.setExp(0);
        }
    }

    @EventHandler
    public void onFightEnd(DuelEndEvent event) {
        Player player1 = event.getWinner().getPlayer();
        final Optional<Guild> guild1 = Guilds.getInstance().getData().getGuild(player1.getUniqueId());
        Player player2 = event.getLoser().getPlayer();
        final Optional<Guild> guild2 = Guilds.getInstance().getData().getGuild(player2.getUniqueId());
        if (guild1.isPresent()) {
            if (guild2.isPresent() && Objects.equals(guild1.get().getName(), guild2.get().getName())) {
                player1.setTotalExperience(0);
                player1.setLevel(0);
                player1.setExp(0);
                player1.giveExp(guild1.get().getLevelExp());
            } else {
                guild1.get().setLevelExp(guild1.get().getLevelExp() + 1);
                this.plugin.getData().saveGuild(guild1.get());
                player1.setTotalExperience(0);
                player1.setLevel(0);
                player1.setExp(0);
                player1.giveExp(guild1.get().getLevelExp());
            }
        }
    }

    @EventHandler(
            priority = EventPriority.LOW
    )
    public void a(AsyncPlayerChatEvent var1) {
        if (!var1.isCancelled()) {
            final Player player = var1.getPlayer();
            final Optional<Guild> guild = this.plugin.getData().getGuild(player.getUniqueId());
            final Optional<GPlayer> gPlayer = this.plugin.getData().getPlayer(player.getUniqueId());
            if (guild.isPresent()) {
                if (gPlayer.isPresent()) {
                    for (final UUID uuid : guild.get().getMembers().keySet()) {
                        final OfflinePlayer member = Bukkit.getOfflinePlayer(uuid);
                        if (member.isOnline()) {
                          if (player.hasMetadata("RadomeGuildChat")) {
                              Message.sendMessage(Objects.requireNonNull(member.getPlayer()), "&2[Lonca] &2&l" + gPlayer.get().getRole() + " &7" + player.getName() + "&8:&f " + var1.getMessage());
                              var1.setCancelled(true);
                          }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player victim = event.getEntity().getPlayer();
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            StrikePracticeAPI api = StrikePractice.getAPI();
            String playerkit = api.getKit(killer).getName();
            String arena = api.getFight(killer).getArena().getName();
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            String command = "battlekit give netheritepotffa " + killer.getName();
            if (Objects.equals(playerkit, "netheritepotffa") && Objects.equals(arena, "ffa")) {
                Bukkit.dispatchCommand(console, command);
                int currentStreak = killStreaks.getOrDefault(killer, 0) + 1;
                killStreaks.put(killer, currentStreak);
                killer.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§6§l⚔ §e" +victim.getName()+ " §7adlı oyuncuyu öldürdünüz! Şu anki öldürme seriniz§e " + currentStreak +"§6🔥"));
                killer.spigot().sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText("§6§l⚔ §e" +victim.getName()+ " §7adlı oyuncuyu öldürdünüz! Şu anki öldürme seriniz§e " + currentStreak +"§6🔥"));
                if (currentStreak % 5 == 0) {
                    Bukkit.broadcastMessage("§6§l⚔ §e" +killer.getName()+ " §7adlı oyuncu §e " + currentStreak +"§6🔥 §7öldürme serisine ulaştı!");
                }
            }
        }
        if (killStreaks.containsKey(victim)) {
            killStreaks.put(victim, 0);
        }
    }

    @EventHandler
    public void onTNTExplosion(EntityExplodeEvent event) {
        event.blockList().clear(); // Blok hasarını önlemek için patlama olayını iptal et.
    }
}

