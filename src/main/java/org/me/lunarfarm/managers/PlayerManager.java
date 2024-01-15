package org.me.lunarfarm.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

import static java.lang.String.format;

public class PlayerManager {
    private long seeds;
    private int level;
    private int fortuna;
    private int multiplicador;
    private int bonus;
    private int blocks_break;
    private Player player;

    public PlayerManager(int blocks_break,long seeds, int level, int fortuna, int multiplicador, int bonus, Player player) {
        this.blocks_break = blocks_break;
        this.seeds = seeds;
        this.level = level;
        this.fortuna = fortuna;
        this.multiplicador = multiplicador;
        this.bonus = bonus;
        this.player = player;
    }

    public int getBlocks_break() {
        return blocks_break;
    }

    public void setBlocks_break(int blocks_break) {
        this.blocks_break = blocks_break;
    }

    public long getSeeds() {
        return seeds;
    }

    public void setSeeds(long seeds) {
        this.seeds = seeds;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFortuna() {
        return fortuna;
    }

    public void setFortuna(int fortuna) {
        this.fortuna = fortuna;
    }

    public int getMultiplicador() {
        return multiplicador;
    }

    public void setMultiplicador(int multiplicador) {
        this.multiplicador = multiplicador;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public Player getPlayer() {
        return player;
    }


}
