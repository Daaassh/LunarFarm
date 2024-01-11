package org.me.lunarfarm.events;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.me.lunarfarm.inventory.EnchantsInventory;
import java.io.IOException;
import java.sql.SQLException;

public class onInteract implements Listener {
    @EventHandler
    public void onInteractPlayer(PlayerInteractEvent event) throws SQLException, IOException, InvalidConfigurationException {
        Player p = event.getPlayer();
            event.setCancelled(true);
            if (p.getItemInHand().getType() == Material.WOOD_HOE) {
                if (p.isSneaking()) {
                    if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                        new EnchantsInventory(p);
                    }
                }
            }
        }
    }
