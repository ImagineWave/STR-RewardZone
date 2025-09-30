package ru.strid.strreward.entities;

import org.bukkit.entity.Player;
import ru.strid.strreward.enums.CooldownType;

import java.util.Map;

public class Cooldown {
    private CooldownType cooldownType;
    private Map<Player, Long> playerCooldowns;
    private long lastUsage;
    private final long cooldownTime;

    public Cooldown (CooldownType cooldownType, int cooldownSeconds) {
        this.cooldownTime = 1000;
        this.cooldownType = cooldownType;
    }

    public Cooldown (int cooldownInSeconds, CooldownType cooldownType) {
        this.cooldownTime = cooldownInSeconds * 1000;
        this.cooldownType = cooldownType;
    }

    public boolean isReady(Player p){
        return false;
    }
}
