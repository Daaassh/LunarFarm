package org.me.lunarfarm.porcentage;

import org.bukkit.Bukkit;

import java.util.Random;


public class PorcentageManager {
    private final Double percentage;

    public PorcentageManager(Double percentage) {
        this.percentage = percentage;
        setup();
    }

    public boolean setup() {
        return verificarPorcentagem(gerarValorAleatorio());
    }

    private boolean verificarPorcentagem(double valor) {
        return valor <= percentage;
    }

    private double gerarValorAleatorio() {
        Random random = new Random();
        return random.nextDouble() * (100 - 0.00001) + 0.00001;
    }
}

