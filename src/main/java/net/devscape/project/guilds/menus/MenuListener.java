// 
// Decompiled by Procyon v0.5.36
// 

package net.devscape.project.guilds.menus;

import net.devscape.project.guilds.Guilds;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.InventoryHolder;
import java.util.Objects;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.Listener;

public class MenuListener implements Listener
{
    @EventHandler
    public void onClick(final InventoryClickEvent e) {
        if (e.getClickedInventory() != null) {
            final InventoryHolder holder = Objects.requireNonNull(e.getClickedInventory()).getHolder();
            if (holder instanceof Menu) {
                e.setCancelled(true);
                if (e.getCurrentItem() == null) {
                    return;
                }
                final Menu menu = (Menu)holder;
                menu.handleMenu(e);
            }
        }
    }
    
    @EventHandler
    public void onClose(final InventoryCloseEvent e) {
        final Player p = (Player) e.getPlayer();
        final InventoryHolder holder = Objects.requireNonNull(e.getInventory()).getHolder();
        if (holder instanceof Menu) {
            Guilds.getMenuUtilMap().remove(p);
        }
    }
}
