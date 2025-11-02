package ru.strid.strreward.services;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.strid.strreward.STRrewardZone;
import ru.strid.strreward.entities.RewardZone;
import ru.strid.strreward.entities.RewardedPlayer;
import ru.strid.strreward.enums.ZoneType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class RewardZoneService {

    private final STRrewardZone plugin;
    private static final Logger log = STRrewardZone.getInstance().getLogger();
    private static final RewardZoneService INSTANCE = new RewardZoneService();
    private static final String REWARD_ZONES_FILE = "rewardZones.yml";
    private static final MessageService messageService = MessageService.getMessageService();

    private final Map<String, RewardZone> rewardZones = new HashMap<>();

    private RewardZoneService() {
        plugin = STRrewardZone.getInstance();
        loadRewardZones();
    }

    public static RewardZoneService getInstance() {
        return INSTANCE;
    }

    public Map<String, RewardZone> getRewardZones() {
        return rewardZones;
    }

    public boolean loadRewardZones() {
        File file = new File(plugin.getDataFolder() + File.separator + plugin.SAVED_DATA_FOLDER_NAME + File.separator + REWARD_ZONES_FILE);
        FileConfiguration rewardsZonesFile = YamlConfiguration.loadConfiguration(file);
        rewardZones.clear();

        if (!file.exists()) {
            log.warning("[RewardService] file not found!: " + file.getName());
            return false;
        }

        if (!rewardsZonesFile.contains("zones")) {
            log.severe("[RewardService] file broken, missing section 'zones'.");
            return false;
        }

        // Пробегаемся по всем ключам в секции "zones"
        int failed = 0;
        for (String key : rewardsZonesFile.getConfigurationSection("zones").getKeys(false)) {
            Object raw = rewardsZonesFile.get("zones." + key);

            if (raw instanceof RewardZone zone) {
                rewardZones.put(zone.getZoneName(), zone);
            } else if (raw instanceof Map) {
                try {
                    RewardZone zone = RewardZone.deserialize((Map<String, Object>) raw);
                    rewardZones.put(zone.getZoneName(), zone);
                } catch (Exception e) {
                    log.severe("Error during loading zone '" + key + "': " + e.getMessage());
                    failed++;
                    e.printStackTrace();
                }
            } else {
                log.severe("Can't deserialize zone: " + key);
                failed++;
            }
        }
        log.info("Loaded zones: " + rewardZones.size());
        if(failed != 0) {
            log.warning("Failed to load " + failed + " reward zones!");
        }
        return true;
    }

    public boolean tryToRewardPlayer(Player player, RewardZone rewardZone) {
        if(canPlayerBeRewarded(player, rewardZone)) {
            if(rewardZone.getZoneType() != ZoneType.EVERY_TIME){
                return processReward(player, rewardZone);
            }
            //Если зона EVERY_TIME
            else {
                if(rewardZone.getCooldown().isReady(player)){
                    boolean rewarded = processReward(player, rewardZone);
                    if(rewarded){
                        rewardZone.getCooldown().setZoneCooldown(player);
                        return rewarded;
                    }
                }
                return false;
            }
        }
        return false;
    }

    private boolean processReward(Player player, RewardZone rewardZone) {
        List<ItemStack> rewardItems = rewardZone.getReward().issueReward(player);
        if(rewardItems.isEmpty()){
            messageService.sendMessage(player, "str.reward.info.emptyRewardContent");
            return false;
        }
        RewardedPlayer rewardedPlayer = new RewardedPlayer(player.getName(), rewardItems, System.currentTimeMillis());
        rewardZone.getRewardedPlayers().add(rewardedPlayer);
        return true;
    }

    private boolean canPlayerBeRewarded(Player player, RewardZone rewardZone) {
        if(rewardZone.getZoneType() == ZoneType.EVERY_TIME){
            return true;
        }
        //Если мы дошли до сюда, и зона оказалась одноразовой - вырубаем ее;
        if(rewardZone.getZoneType() == ZoneType.SINGLE_TIME){
            rewardZone.setActive(false);
            return true;
        }
        if(rewardZone.getZoneType() == ZoneType.SINGLE_PER_PLAYER){
            for(RewardedPlayer rewardedPlayer : rewardZone.getRewardedPlayers()){
                if(rewardedPlayer.getPlayerName().equals(player.getName())){
                    return false;
                }
            }
            return true;
        }
        //На случай если не попали не в олин иф (хотя такое не возможно)
        return false;
    }

    public void saveRewardZone(RewardZone rewardZone) {
        rewardZones.put(rewardZone.getZoneName(), rewardZone);
        saveRewardZones();
    }

    public void saveRewardZones() {
        File file = new File(plugin.getDataFolder() + File.separator + plugin.SAVED_DATA_FOLDER_NAME + File.separator + REWARD_ZONES_FILE);
        FileConfiguration rewardsZonesFile = YamlConfiguration.loadConfiguration(file);
        for (Map.Entry<String, RewardZone> entry : rewardZones.entrySet()) {
            rewardsZonesFile.set("zones." + entry.getKey(), entry.getValue());
        }

        try {
            rewardsZonesFile.save(file);
            log.info("Saved zones: " + rewardZones.size());
        } catch (IOException e) {
            log.severe("Error during saving reward zones file: :");
            e.printStackTrace();
        }
    }


}
