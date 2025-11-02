package ru.strid.strreward.services;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.strid.strreward.STRrewardZone;
import ru.strid.strreward.entities.Reward;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RewardService {
    private final STRrewardZone plugin;
    private static final RewardService INSTANCE = new RewardService();
    private static final String REWARDS_FILE = "rewards.yml";
    private final Map<String, Reward> rewards = new HashMap<>();

    private RewardService() {
        plugin = STRrewardZone.getInstance();
        loadRewards();
    }

    public static RewardService getInstance() {
        return INSTANCE;
    }

    public Map<String, Reward> getRewards() {
        return rewards;
    }

    public Reward getOrCreateReward(String rewardTableName) {
        Reward rewardFromCache = RewardService.getInstance().getRewards().get(rewardTableName);
        Reward actualReward = rewardFromCache != null ? rewardFromCache : new Reward(rewardTableName);
        rewards.put(rewardTableName, actualReward);
        saveRewards();
        return actualReward;
    }

    public void saveRewards() {
        File file = new File(plugin.getDataFolder() + File.separator + plugin.SAVED_DATA_FOLDER_NAME + File.separator + REWARDS_FILE);
        FileConfiguration rewardsFile = YamlConfiguration.loadConfiguration(file);
        for (Map.Entry<String, Reward> entry : rewards.entrySet()) {
            rewardsFile.set("rewards." + entry.getKey(), entry.getValue());
        }
        try {
            rewardsFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRewards() {
        File file = new File(plugin.getDataFolder() + File.separator + plugin.SAVED_DATA_FOLDER_NAME + File.separator + REWARDS_FILE);
        FileConfiguration rewardsFile = YamlConfiguration.loadConfiguration(file);
        rewards.clear();
        if (!file.exists()) return;
        ConfigurationSection section = rewardsFile.getConfigurationSection("rewards");
        if (section == null) return;
        for (String key : section.getKeys(false)) {
            Reward reward = (Reward) section.get(key);
            if (reward != null) {
                rewards.put(key, reward);
            }
        }
    }
}
