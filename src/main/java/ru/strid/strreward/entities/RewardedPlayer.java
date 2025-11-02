package ru.strid.strreward.entities;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

import java.util.*;


@SerializableAs("RewardedPlayer")
public class RewardedPlayer implements ConfigurationSerializable {
    private String playerName;
    private List<ItemStack> items;
    private long rewardedTime;

    public RewardedPlayer(String playerName, List<ItemStack> items, long rewardedTime) {
        this.playerName = playerName;
        this.items = items;
        this.rewardedTime = rewardedTime;
    }

    public String getPlayerName() {
        return playerName;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public long getRewardedTime() {
        return rewardedTime;
    }

    // --- Сериализация в Map для YAML ---
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("playerName", playerName);
        map.put("items", items);
        map.put("rewardedTime", rewardedTime);
        return map;
    }

    // --- Десериализация из Map (Bukkit вызывает сам при загрузке из YML) ---
    @SuppressWarnings("unchecked")
    public static RewardedPlayer deserialize(Map<String, Object> map) {
        String playerName = (String) map.get("playerName");
        List<ItemStack> items = (List<ItemStack>) map.getOrDefault("items", new ArrayList<>());
        long rewardedTime = ((Number) map.getOrDefault("rewardedTime", 0L)).longValue();

        return new RewardedPlayer(playerName, items, rewardedTime);
    }

    @Override
    public String toString() {
        return "RewardedPlayer{" +
                "playerName='" + playerName + '\'' +
                ", items=" + items +
                ", rewardedTime=" + rewardedTime +
                '}';
    }
}
