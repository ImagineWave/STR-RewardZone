package ru.strid.strreward.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.strid.strreward.services.MessageService;

public class TestListener implements Listener {

    private MessageService messageService = MessageService.getMessageService();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        p.sendMessage("raw locale: "+p.getLocale());
        messageService.sendMessage(p, "str.reward.test.testLocaleMessage", "str.reward.common.player", p.getDisplayName(), "str.reward.common.rewardZone");
    }

}
