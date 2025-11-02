package ru.strid.strreward.entities;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.SerializableAs;
import ru.strid.strreward.entities.impl.RegionImpl;
import ru.strid.strreward.enums.ZoneType;
import ru.strid.strreward.services.RewardService;

import java.util.*;

@SerializableAs("RewardZone")
public class RewardZone implements ConfigurationSerializable {

    private final String zoneName;
    private boolean isActive; //Зона может быть отключена (вручную или например если она была одноразовой)
    private List<RewardedPlayer> rewardedPlayers = new ArrayList<>(); //Хранение данных о получивших игроках награду
    private Region region; //Мир и место в котором находится зона. Нам важен внешний метод isInRegion()
    private ZoneType zoneType;
    private Reward reward;
    private Cooldown cooldown;


    public RewardZone(String zoneName, Region region, Reward reward) {
        this.zoneName = zoneName;
        this.region = region;
        this.zoneType = ZoneType.SINGLE_PER_PLAYER;
        this.reward = reward;
        this.cooldown = new Cooldown(0);
    }


    public String getZoneName() {
        return zoneName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<RewardedPlayer> getRewardedPlayers() {
        return rewardedPlayers;
    }

    public void setRewardedPlayers(List<RewardedPlayer> rewardedPlayers) {
        this.rewardedPlayers = rewardedPlayers;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public ZoneType getZoneType() {
        return zoneType;
    }

    public void setZoneType(ZoneType zoneType) {
        this.zoneType = zoneType;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public Cooldown getCooldown() {
        return cooldown;
    }

    public void setCooldown(Cooldown cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("zoneName", zoneName);
        map.put("isActive", isActive);
        map.put("rewardedPlayers", rewardedPlayers);
        map.put("region", region instanceof RegionImpl r ? r.toMap() : null);
        map.put("zoneType", zoneType.name());
        map.put("reward", reward.getRewardTableName());
        map.put("cooldown", cooldown);
        return map;
    }

//    @SuppressWarnings("unchecked")
//    public static RewardZone deserialize(Map<String, Object> map) {
//        String zoneName = (String) map.get("zoneName");
//        boolean isActive = (boolean) map.getOrDefault("isActive", true);
//
//        List<RewardedPlayer> rewardedPlayers =
//                (List<RewardedPlayer>) map.getOrDefault("rewardedPlayers", new ArrayList<>());
//
//        Map<String, Object> regionMap = (Map<String, Object>) map.get("region");
//        Region region = regionMap != null ? RegionImpl.fromMap(regionMap) : null;
//
//        ZoneType zoneType = ZoneType.valueOf((String) map.get("zoneType"));
//        Reward reward = RewardService.getInstance().getRewards().get((String) map.get("reward"));
//        Cooldown cooldown = (Cooldown) map.get("cooldown");
//
//        RewardZone output = new RewardZone(zoneName, region, reward);
//        output.setReward(reward);
//        output.setCooldown(cooldown);
//        output.setZoneType(zoneType);
//        output.setActive(isActive);
//
//        return output;
//    }
    @SuppressWarnings("unchecked")
    public static RewardZone deserialize(Map<String, Object> map) {
        String zoneName = (String) map.get("zoneName");
        boolean isActive = (boolean) map.getOrDefault("isActive", true);

        List<RewardedPlayer> rewardedPlayers = new ArrayList<>();
        List<Object> rawList = (List<Object>) map.get("rewardedPlayers");
        if (rawList != null) {
            for (Object obj : rawList) {
                if (obj instanceof RewardedPlayer rp) {
                    rewardedPlayers.add(rp);
                } else if (obj instanceof Map) {
                    RewardedPlayer rp = (RewardedPlayer) ConfigurationSerialization.deserializeObject(
                            (Map<String, Object>) obj, RewardedPlayer.class
                    );
                    rewardedPlayers.add(rp);
                }
            }
        }

        Map<String, Object> regionMap = (Map<String, Object>) map.get("region");
        Region region = regionMap != null ? RegionImpl.fromMap(regionMap) : null;

        ZoneType zoneType = ZoneType.valueOf((String) map.get("zoneType"));
        Reward reward = RewardService.getInstance().getRewards().get((String) map.get("reward"));
        Cooldown cooldown = (Cooldown) map.get("cooldown");

        RewardZone output = new RewardZone(zoneName, region, reward);
        output.setReward(reward);
        output.setCooldown(cooldown);
        output.setZoneType(zoneType);
        output.setActive(isActive);
        output.setRewardedPlayers(rewardedPlayers); // не забудь добавить этот сеттер

        return output;
    }

}
