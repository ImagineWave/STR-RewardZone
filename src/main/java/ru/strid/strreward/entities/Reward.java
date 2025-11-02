package ru.strid.strreward.entities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.strid.strreward.enums.MessageType;
import ru.strid.strreward.enums.RewardType;
import ru.strid.strreward.services.MessageService;

import java.util.*;

@SerializableAs("Reward")
public class Reward implements ConfigurationSerializable {
    private final String rewardTableName;
    private RewardType rewardType;
    private RewardContent rewardContent;
    private RewardContent usedRewardContent;
    private boolean depletable;
    private int numberOfSlots = 0;

    public Reward(String rewardTableName) {
        this.rewardTableName = rewardTableName;
        this.rewardType = RewardType.FULL_TABLE;
        ItemStack item = new ItemStack(Material.IRON_INGOT);
        item.setAmount(1);
        this.rewardContent = new RewardContent(item);
        this.usedRewardContent = new RewardContent();
        this.depletable = false;
    }

    public String getRewardTableName() {
        return rewardTableName;
    }

    public RewardType getRewardType() {
        return rewardType;
    }

    public void setRewardType(RewardType rewardType) {
        this.rewardType = rewardType;
    }

    public RewardContent getRewardContent() {
        return rewardContent;
    }

    public void setRewardContent(RewardContent rewardContent) {
        this.rewardContent = rewardContent;
    }

    public RewardContent getUsedRewardContent() {
        return usedRewardContent;
    }

    public void setUsedRewardContent(RewardContent usedRewardContent) {
        this.usedRewardContent = usedRewardContent;
    }

    public boolean isDepletable() {
        return depletable;
    }

    public void setDepletable(boolean depletable) {
        this.depletable = depletable;
    }

    public int getNumberOfSlots() {
        return numberOfSlots;
    }

    public void setNumberOfSlots(int numberOfSlots) {
        this.numberOfSlots = numberOfSlots;
    }

    public List<ItemStack> issueReward(Player player) {
        List<ItemStack> rewardItemsCandidates = rewardContent.getItems();
        rewardItemsCandidates.removeAll(usedRewardContent.getItems());
        if(numberOfSlots == 0) {
            if(depletable) {
                usedRewardContent.getItems().addAll(rewardItemsCandidates);
            }
            giveItems(player, rewardItemsCandidates);
            return rewardItemsCandidates;
        }
        List<ItemStack> actualReward = new ArrayList<>();
        for(int i = 0; i < numberOfSlots; i++) {
            Random random = new Random();
            int index = random.nextInt(rewardItemsCandidates.size()-1);
            actualReward.add(rewardItemsCandidates.get(index));
            rewardItemsCandidates.remove(index);
        }
        giveItems(player, actualReward);
        return actualReward;
    }

    private void giveItems(Player player, List<ItemStack> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        Location loc = player.getLocation();
        for (ItemStack item : items) {
            if (item == null) continue;
            Map<Integer, ItemStack> leftover = player.getInventory().addItem(item);
            // Если не всё поместилось — выбрасываем остатки
            if (!leftover.isEmpty()) {
                for (ItemStack remaining : leftover.values()) {
                    player.getWorld().dropItemNaturally(loc, remaining);
                }
            }
        }
        // Сообщение только один раз (если хотя бы что-то не влезло)
        boolean full = player.getInventory().firstEmpty() == -1;
        if (full) {
            MessageService.getMessageService().sendMessage(player, MessageType.BAD, "str.reward.info.fullInventoryWarn");
        }
    }

    public Reward(Map<String, Object> map) {
        this.rewardTableName = (String) map.get("rewardTableName");
        this.rewardType = RewardType.valueOf((String) map.get("rewardType"));
        this.rewardContent = (RewardContent) map.get("rewardContent");
        this.usedRewardContent = (RewardContent) map.get("usedRewardContent");
        this.depletable = (boolean) map.getOrDefault("depletable", false);
        this.numberOfSlots = (int) map.getOrDefault("numberOfSlots", 0);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("rewardTableName", rewardTableName);
        map.put("rewardType", rewardType.name());
        map.put("rewardContent", rewardContent);
        map.put("usedRewardContent", usedRewardContent);
        map.put("depletable", depletable);
        map.put("numberOfSlots", numberOfSlots);
        return map;
    }

}
