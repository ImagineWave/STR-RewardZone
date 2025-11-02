package ru.strid.strreward.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.strid.strreward.entities.Region;
import ru.strid.strreward.entities.RewardZone;
import ru.strid.strreward.enums.MessageType;
import ru.strid.strreward.services.MessageService;
import ru.strid.strreward.services.RegionService;
import ru.strid.strreward.services.RewardService;
import ru.strid.strreward.services.RewardZoneService;

public class RewardCRUDCommand implements CommandExecutor {

    private final MessageService messageService = MessageService.getMessageService();
    private final RegionService regionService = RegionService.getInstance();
    private final RewardZoneService rewardZoneService = RewardZoneService.getInstance();
    private final RewardService rewardService = RewardService.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("str.reward.crud")) {
            messageService.sendMessage(sender, MessageType.BAD, "str.reward.crud.noPermission");
            return true;
        }
        switch(args[0]) {
            case "create": {
                createRewardZone(sender, args);
                return true;
            }
            case "edit": {
                updateRewardZone(sender, args);
                return true;
            }
            case "delete": {
                deleteRewardZone(sender, args);
                return true;
            }
            default: {

                return false;
            }
        }
    }

    private boolean createRewardZone(CommandSender sender, String[] args){
        Player player = (Player) sender;
        String zoneName = args[1];
        String rewardTableName = args[2];
        Region region = regionService.getSelectedRegion(player);
        RewardZone rewardZone = new RewardZone(zoneName, region, rewardService.getOrCreateReward(rewardTableName));
        rewardZoneService.getRewardZones().put(zoneName, rewardZone);
        rewardZoneService.saveRewardZones();
        messageService.sendMessage(sender, "str.reward.crud.successfullyCreatedRewardZone");
        return false;
    }

    private boolean updateRewardZone(CommandSender sender, String[] args){
        return false;
    }

    private boolean deleteRewardZone(CommandSender sender, String[] args){
        return false;
    }
}
