package ru.strid.strreward.entities;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@SerializableAs("RewardContent")
public class RewardContent implements ConfigurationSerializable {
    private List<ItemStack> items;

    public RewardContent() {
        items = new ArrayList<>();
    }
    public RewardContent(ItemStack... items) {
        this.items = new ArrayList<>();
        this.items.addAll(Arrays.asList(items));
    }

    public List<ItemStack> getItems() {
        return items;
    }
    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public RewardContent(Map<String, Object> map) {
        this.items = (List<ItemStack>) map.get("items");
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("items", items);
        return map;
    }

    //Тут будет хранитсья лут конкретной зоны, и была ли выдана каждая единица лута (если используется истощаемый пул)
}
