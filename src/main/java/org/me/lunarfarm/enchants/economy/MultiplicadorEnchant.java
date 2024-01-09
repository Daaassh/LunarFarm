package org.me.lunarfarm.enchants.economy;

import org.me.lunarfarm.managers.PlayerManager;

public class MultiplicadorEnchant {
    private Integer blocks;
    private PlayerManager manager;

    public MultiplicadorEnchant(PlayerManager manager,Integer blocks) {
        this.blocks = blocks;
    }
    public void setup(){
        manager.setSeeds(manager.getSeeds() + (blocks * manager.getMultiplicador()));
    }
}
