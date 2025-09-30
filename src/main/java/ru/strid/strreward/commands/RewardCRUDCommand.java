package ru.strid.strreward.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.strid.strreward.enums.MessageType;
import ru.strid.strreward.services.MessageService;
import ru.strid.strreward.services.RegionService;

public class RewardCRUDCommand implements CommandExecutor {

    private MessageService messageService = MessageService.getMessageService();
    private RegionService regionService = RegionService.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("str.reward.crud")) {
            messageService.sendMessage(sender, MessageType.BAD, "str.reward.crud.noPermission");
            return true;
        }

        return false;
    }

    private boolean createReward(CommandSender sender, String[] args){
        return false;
    }

    private boolean updateReward(CommandSender sender, String[] args){
        return false;
    }

    private boolean deleteReward(CommandSender sender, String[] args){
        return false;
    }
}
