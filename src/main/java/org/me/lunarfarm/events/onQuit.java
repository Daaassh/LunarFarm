package org.me.lunarfarm.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.me.lunarfarm.cache.PlayersInFarm;
import org.me.lunarfarm.database.MySqlUtils;

import java.util.Objects;

public class onQuit implements Listener
{
    @EventHandler
    public void onQuits(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        MySqlUtils.updatePlayer(Objects.requireNonNull(PlayersInFarm.cache.getIfPresent(p.getUniqueId())), p);
        PlayersInFarm.playersInFarm.remove(p.getUniqueId());
    }
}
