package org.me.lunarfarm.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.me.lunarfarm.cache.PlayersInFarm;
import org.me.lunarfarm.database.MySqlUtils;
import org.me.lunarfarm.managers.PlayerManager;

public class onJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        try {
            Player p = e.getPlayer();
            MySqlUtils.existPlayer(p);
            PlayerManager manager = MySqlUtils.getPlayer(p);
            PlayersInFarm.cache.put(p.getUniqueId(), manager);

        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
