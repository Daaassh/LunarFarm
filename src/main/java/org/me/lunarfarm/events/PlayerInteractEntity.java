package org.me.lunarfarm.events;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class PlayerInteractEntity implements Listener {
    @EventHandler
    public void onInteractEntity(PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity instanceof Item) {
            Item item = (Item) entity;
            if (item.getItemStack().getType() == Material.CROPS) {
                event.setCancelled(true);
            }
        }
    }
}
