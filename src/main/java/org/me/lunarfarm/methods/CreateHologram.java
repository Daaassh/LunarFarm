package org.me.lunarfarm.methods;

import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.HologramBuilder;
import com.github.unldenis.hologram.HologramPool;
import com.github.unldenis.hologram.animation.Animation;
import com.github.unldenis.hologram.placeholder.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.me.lunarfarm.Main;
import static java.lang.String.format;

public class CreateHologram {
    private final ItemStack item;
    private final Player player;
    private final Double seeds;
    private final Location loc;
    private final FileConfiguration config = Main.getPlugin(Main.class).getConfig();
    private final Placeholders placeholders = new Placeholders();

    private Hologram holograms = null;
    public CreateHologram(ItemStack item, Player player, Double seeds, Location loc) {
        this.seeds = seeds;
        this.loc = loc;
        this.item = item;
        this.player = player;
    }


    public void create() {
        Location hologramLocation = loc.clone().add(0, 0.0, 0);

        HologramPool pool = new HologramPool(Main.getPlugin(Main.class), 0);
        HologramBuilder builder = Hologram.builder(Main.getPlugin(Main.class), loc, placeholders)
                .addItemLine(new ItemStack(Material.HAY_BLOCK), Animation.AnimationType.CIRCLE)
                .addTextLine("")
                .addTextLine(ChatColor.YELLOW + "+" + getFormat(seeds))
                .addTextLine("");

        if (item.getType() == Material.WHEAT) {
            builder.addItemLine(new ItemStack(Material.SEEDS), Animation.AnimationType.CIRCLE);
        }

        holograms = builder.loadAndBuild(pool);

        for (Player p : Bukkit.getOnlinePlayers()) {
            holograms.hide(p);
            if (p.getUniqueId() == player.getUniqueId()) {
                holograms.show(p);
            }
        }

        double initialHeight = hologramLocation.getY();
        double targetHeight = hologramLocation.getY() + config.getDouble("break_farms.hologram-y");
        new BukkitRunnable() {
            private double currentHeight = initialHeight;

            @Override
            public void run() {
                Location location = new Location(hologramLocation.getWorld(),hologramLocation.getX(), currentHeight, hologramLocation.getZ(), hologramLocation.getYaw(), hologramLocation.getPitch());
                holograms.teleport(location);
                currentHeight += 0.1;
                if (currentHeight >= targetHeight) {
                    pool.remove(holograms);
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 0L, config.getLong("break_farms.hologram-speed"));
    }

    public String getFormat(double valor) {
        String[] simbols = new String[]{"", "K", "M", "B", "T", "Q", "QQ", "S", "SS", "O", "N", "D", "UN", "DD", "TD",
                "QD", "QID", "SD", "SSD", "OD", "ND", "V", "UV", "DV", "TV", "QV", "QQV", "SV", "SSV", "OV", "NV", "TG",
                "UTG", "DTG", "TTG", "QTG", "QQTG", "STG", "SSTG", "OTG", "NTG", "QG"};
        int index;
        for (index = 0; valor / 1000.0 >= 1.0; valor /= 1000.0, ++index) {
        }
        String valors = format("%.2f", valor);
        return format(valors) + simbols[index];
    }
}
