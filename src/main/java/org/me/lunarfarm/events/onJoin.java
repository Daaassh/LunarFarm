package org.me.lunarfarm.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.me.lunarfarm.database.MySqlUtils;
import org.me.lunarfarm.hoe.CreateHoe;

public class onJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        try {
            Player p = e.getPlayer();
            MySqlUtils.existPlayer(p);
            new CreateHoe(MySqlUtils.getPlayer(p));
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
