package ru.strid.strreward.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.strid.strreward.STRrewardZone;
import ru.strid.strreward.entities.RewardZone;
import ru.strid.strreward.enums.MessageType;
import ru.strid.strreward.events.PlayerEnterRewardZoneEvent;
import ru.strid.strreward.services.MessageService;
import ru.strid.strreward.services.RewardZoneService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class RewardZoneListener implements Listener {
    private final Logger log = STRrewardZone.getInstance().getLogger();
    private final RewardZoneService rewardZoneService = RewardZoneService.getInstance();
    private final MessageService messageService = MessageService.getMessageService();
    private final Map<Player, Set<String>> playerInsideZones = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlock().equals(event.getTo().getBlock())) return;
        Player player = event.getPlayer();
        Set<String> insideZones = playerInsideZones.computeIfAbsent(player, k -> new HashSet<>());

        for (RewardZone zone : RewardZoneService.getInstance().getRewardZones().values()) {
            if (!zone.isActive()) continue;
            boolean isInside = zone.getRegion().isInside(event.getTo());
            if (isInside && !insideZones.contains(zone.getZoneName())) {
                // Вход в зону
                PlayerEnterRewardZoneEvent customEvent = new PlayerEnterRewardZoneEvent(player, zone);
                Bukkit.getPluginManager().callEvent(customEvent);

                insideZones.add(zone.getZoneName()); // Помечаем игрока как находящегося в зоне
            } else if (!isInside && insideZones.contains(zone.getZoneName())) {
                // Выход из зоны
                insideZones.remove(zone.getZoneName());
            }
        }
    }

    @EventHandler
    public void onPlayerReachZone(PlayerEnterRewardZoneEvent event) {
        log.info("Player " + event.getPlayer().getName() + " entered reward zone "+ event.getRewardZone().getZoneName());
        if(rewardZoneService.tryToRewardPlayer(event.getPlayer(), event.getRewardZone())){
            log.info("Player " + event.getPlayer().getName() + " got rewards of zone "+ event.getRewardZone().getZoneName());
            messageService.sendMessage(event.getPlayer(), MessageType.GOOD, "str.reward.info.playerGotReward");
            //TODO Записать всю награду в лог файл
        }
    }

}
