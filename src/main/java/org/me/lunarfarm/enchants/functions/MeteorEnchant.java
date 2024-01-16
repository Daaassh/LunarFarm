package org.me.lunarfarm.enchants.functions;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.managers.PlayerManager;

public class MeteorEnchant {

    private final FileConfiguration config = Main.getPlugin(Main.class).getConfig();
    private Location location;

    public MeteorEnchant(Location location) {
        this.location = location;
    }
    private void setup(){
         new BukkitRunnable() {

            @Override
            public void run() {
                double initialHeight = location.getY();
                double currentHeight = initialHeight;
                Double targetLocationY = location.getY() + -config.getDouble("meteor.y");
                currentHeight -= 0.1;
                Fireball fireball = (Fireball) location.getWorld().spawnEntity(location.add(0, currentHeight, 0), EntityType.FIREBALL);
                if (currentHeight <= targetLocationY) {
                    int initialx = (int) location.getX() / 2;
                    for (int x = initialx; x < 3; x++) {
                        fireball.setDirection(new Vector(x,targetLocationY,location.getZ()));
                        Location loc = new Location(location.getWorld(), x, location.getY(), location.getZ());
                        if (location.getWorld().getBlockAt(loc).getType() == Material.WHEAT) {
                            location.getWorld().getBlockAt(loc).setType(Material.AIR);
                        }
                    }
                    cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 1L);
    }
    private void createHologram(){
        Holog
    }

}
