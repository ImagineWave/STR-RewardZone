package ru.strid.strreward.entities;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RewardContent {
    private List<ItemStack> items;

    public RewardContent() {
        items = new ArrayList<>();
    }

    public List<ItemStack> getItems() {
        return items;
    }
    public void setItems(List<ItemStack> items) {
        this.items = items;
    }


    //Тут будет хранитсья лут конкретной зоны, и была ли выдана каждая единица лута (если используется истощаемый пул)
}
