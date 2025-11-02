package ru.strid.strreward.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.strid.strreward.enums.MessageType;
import ru.strid.strreward.services.MessageService;
import ru.strid.strreward.services.RewardZoneService;

public class LoadZonesCommand implements CommandExecutor {

    private final MessageService messageService = MessageService.getMessageService();
    private final RewardZoneService rewardZoneService = RewardZoneService.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("str.reward.crud")) {
            messageService.sendMessage(sender, MessageType.BAD, "str.reward.crud.noPermission");
            return true;
        }
        if(rewardZoneService.loadRewardZones()){
            messageService.sendMessage(sender, MessageType.GOOD, "str.reward.crud.dataReloadSuccess");
            return true;
        }else {
            messageService.sendMessage(sender, MessageType.BAD, "str.reward.crud.dataReloadFailure");
            return true;
        }
    }
}
