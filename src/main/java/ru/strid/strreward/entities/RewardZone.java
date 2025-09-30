package ru.strid.strreward.entities;

import ru.strid.strreward.enums.ZoneType;

import java.util.List;

public class RewardZone {

    private String zoneName;
    private boolean isActive; //Зона может быть отключена (вручную или например если она была одноразовой)
    private List<RewardedPlayer> rewardedPlayers; //Хранение данных о получивших игроках награду
    private Region region; //Мир и место в котором находится зона. Нам важен внешний метод isInRegion()
    private ZoneType zoneType;
    private Reward reward;
    private Cooldown cooldown;


    public RewardZone(String zoneName, Region region, String rewardTableName) {
        this.zoneName = zoneName;
        this.region = region;
        this.zoneType = ZoneType.SINGLE_PER_PLAYER;
        this.reward = new Reward(rewardTableName);
    }


    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
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

}
