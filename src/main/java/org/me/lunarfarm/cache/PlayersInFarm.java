package org.me.lunarfarm.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.bukkit.entity.Player;

import org.me.lunarfarm.managers.PlayerManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayersInFarm {
    public static Map<UUID,Player> playersInFarm = new HashMap<>();
    public static Cache<UUID, PlayerManager> cache = Caffeine.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();
}
