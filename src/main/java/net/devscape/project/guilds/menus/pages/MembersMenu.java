package net.devscape.project.guilds.menus.pages;

import ga.strikepractice.StrikePractice;
import ga.strikepractice.stats.PlayerStats;
import net.devscape.project.guilds.Guilds;
import net.devscape.project.guilds.menus.Menu;
import net.devscape.project.guilds.menus.MenuUtil;
import net.devscape.project.guilds.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

import static net.devscape.project.guilds.util.Message.deformat;
import static net.devscape.project.guilds.util.Message.format;

public class MembersMenu extends Menu {

    public MembersMenu(MenuUtil menuUtil) {
        super(menuUtil);
    }

    @Override
    public String getMenuName() {
        return "[lang]guilds.membersmenu.info[/lang]";
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);

        if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
            String member = deformat(e.getCurrentItem().getItemMeta().getDisplayName());

            OfflinePlayer m = Bukkit.getOfflinePlayer(member);
            OfflinePlayer o = Bukkit.getOfflinePlayer(menuUtil.getGuild().getOwner());

            if (menuUtil.getGuild().getMembers().containsKey(m.getUniqueId())) {
                if (!m.getUniqueId().equals(o.getUniqueId())) {
                    if (!m.getUniqueId().equals(player.getUniqueId())) {
                        menuUtil.getGuild().removeMember(m.getUniqueId());
                        Guilds.getInstance().getData().removePlayer(m.getUniqueId());
                        Guilds.getInstance().getData().saveGuild(menuUtil.getGuild());
                        player.closeInventory();
                        new MembersMenu(Guilds.getMenuUtil((Player) e.getWhoClicked(), menuUtil.getGuild())).open();
                        player.sendMessage(format("[lang]guilds.membersmenu.remove[/lang]"));
                    } else {
                        player.closeInventory();
                        player.sendMessage(format("[lang]guilds.membersmenu.cannot-remove-yourself[/lang]"));
                    }
                } else {
                    player.closeInventory();
                    player.sendMessage(format("[lang]guilds.membersmenu.leaderremove[/lang]"));
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        for (UUID uuid : menuUtil.getGuild().getMembers().keySet()) {
            OfflinePlayer member = Bukkit.getOfflinePlayer(uuid);

            OfflinePlayer m = Bukkit.getOfflinePlayer(uuid);
            OfflinePlayer o = Bukkit.getOfflinePlayer(menuUtil.getGuild().getOwner());

            if (!m.getUniqueId().equals(o.getUniqueId())) {
                this.inventory.addItem(this.makeSkull(Message.format("&a" + member.getName()), format("&7"), format("[lang]guilds.membersmenu.clicktoremove[/lang]")));
            }

            if (menuUtil.getGuild().getMembers().size() <= 1) {
                this.inventory.setItem(22, this.makeItem(Material.HOPPER, Message.format("[lang]guilds.membersmenu.nomember[/lang]")));
            }
        }
    }
}