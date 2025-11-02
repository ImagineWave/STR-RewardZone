package ru.strid.strreward.entities;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import ru.strid.strreward.enums.CooldownType;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SerializableAs("Cooldown")
public class Cooldown implements ConfigurationSerializable {
    private CooldownType cooldownType;
    private Map<String, Long> playerCooldowns;
    private long lastUsage;
    private final long cooldownTime;


    public Cooldown (long cooldownInSeconds){
        this.cooldownTime = cooldownInSeconds * 1000;
        if(cooldownInSeconds == 0){
            this.cooldownType = CooldownType.NO_COOLDOWN;
        } else {
            this.cooldownType = CooldownType.GENERAL;
        }
    }

    public boolean isReady(Player player){
        if(cooldownType == CooldownType.NO_COOLDOWN){
            return true;
        }
        if(cooldownType == CooldownType.GENERAL){
            return lastUsage + cooldownTime < System.currentTimeMillis();
        }
        if(cooldownType == CooldownType.PER_PLAYER){
            if(playerCooldowns.containsKey(player.getName())){
               return playerCooldowns.get(player.getName()) + cooldownTime < System.currentTimeMillis();
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();

        // Тип кулдауна
        map.put("cooldownType", cooldownType != null ? cooldownType.name() : null);

        // Кулдаун по игрокам (имена уже String)
        map.put("playerCooldowns", playerCooldowns != null ? new HashMap<>(playerCooldowns) : new HashMap<>());

        // Последнее использование и общее время кулдауна
        map.put("lastUsage", lastUsage);
        map.put("cooldownTime", cooldownTime);

        return map;
    }

    public void setZoneCooldown(Player player){
        if(this.cooldownType==CooldownType.NO_COOLDOWN){
            return;
        }
        if(this.cooldownType==CooldownType.GENERAL){
            this.lastUsage = System.currentTimeMillis();
            return;
        }
        if(this.cooldownType==CooldownType.PER_PLAYER){
            playerCooldowns.put(player.getName(), System.currentTimeMillis());
            return;
        }
    }

    @SuppressWarnings("unchecked")
    public static Cooldown deserialize(Map<String, Object> map) {
        // Тип кулдауна
        String typeName = (String) map.get("cooldownType");
        CooldownType cooldownType = typeName != null ? CooldownType.valueOf(typeName) : null;

        // Время последнего использования и общий кулдаун
        long lastUsage = ((Number) map.getOrDefault("lastUsage", 0L)).longValue();
        long cooldownTime = ((Number) map.getOrDefault("cooldownTime", 0L)).longValue();

        // Создаём объект
        Cooldown cooldown = new Cooldown(cooldownTime/1000);
        cooldown.cooldownType = cooldownType;
        cooldown.lastUsage = lastUsage;

        // Кулдаун по игрокам
        Map<String, Long> cooldownsMap = (Map<String, Long>) map.getOrDefault("playerCooldowns", new HashMap<>());
        cooldown.playerCooldowns = new HashMap<>(cooldownsMap);

        return cooldown;
    }
}
