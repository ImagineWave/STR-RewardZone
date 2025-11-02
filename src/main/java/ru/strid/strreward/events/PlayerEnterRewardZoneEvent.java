package ru.strid.strreward.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ru.strid.strreward.entities.RewardZone;

public class PlayerEnterRewardZoneEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final RewardZone rewardZone;

    public PlayerEnterRewardZoneEvent(Player player, RewardZone rewardZone) {
        this.player = player;
        this.rewardZone = rewardZone;
    }

    public Player getPlayer() {
        return player;
    }

    public RewardZone getRewardZone() {
        return rewardZone;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
