package ru.strid.strreward.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.strid.strreward.STRrewardZone;
import ru.strid.strreward.entities.BlockPosition;
import ru.strid.strreward.entities.Region;
import ru.strid.strreward.entities.impl.RegionImpl;
import ru.strid.strreward.enums.MessageType;
import ru.strid.strreward.services.MessageService;
import ru.strid.strreward.services.RegionService;

import java.util.logging.Logger;

public class RegionSelectCommand implements CommandExecutor {

    private MessageService messageService = MessageService.getMessageService();
    private RegionService regionService = RegionService.getInstance();
    private final Logger logger = STRrewardZone.getInstance().getLogger();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if(!sender.hasPermission("str.reward.crud")) {
                messageService.sendMessage(sender, MessageType.BAD, "str.reward.crud.noPermission");
                logger.info(sender.getName() + " " + sender.hasPermission("tried to use regionselect command"));
                return true;
            }
            if(!(sender instanceof Player)) {
                messageService.sendMessage(sender, MessageType.BAD, "str.reward.crud.onlyPlayerUsage");
                return true;
            }
            Player player = (Player) sender;
            if(args.length == 0) {
                //Информация о выделенном регионе + инструкция как сбросить РГ
                showRgInfo(player);
                return true;
            }
            if(args.length == 1) {
                if(args[0].equals("clear")){
                    clearRegion(player);
                    return true;
                }
            }
            if((args.length >= 1)&&(args.length <= 4)) {
                selectByPlayerPosition(player, args);
                return true;
            }
            if(args.length == 6) {
                selectByExactArgs(player, args);
                return true;
            }
        } catch (Exception e){
            e.printStackTrace();
            logger.warning(e.getMessage());
            messageService.sendMessage(sender, MessageType.BAD, "str.reward.common.error");
            return true;
        }
        return false;
    }

    private void showRgInfo(Player player) {
        if(regionService.getSelectedRegion(player)!=null){
            messageService.sendMessage(player, regionService.getSelectedRegion(player).toString());
        } else {
            messageService.sendMessage(player, MessageType.INFO, "str.reward.crud.region.noSelectedRegion");
        }
        messageService.sendMessage(player, MessageType.INFO, "str.reward.crud.region.howToClear");
    }

    private void clearRegion(Player player) {
        regionService.removeSelectedRegion(player);
        messageService.sendMessage(player, MessageType.GOOD, "str.reward.crud.region.clearedRegion");
    }

    private void selectByPlayerPosition(Player player, String[] args) throws IllegalArgumentException {
        if((args.length >= 1)) {
            Region region = regionService.getSelectedRegion(player);
            if(region == null) {
                region = new RegionImpl();
            }
            Location loc = player.getLocation();
            if(args.length == 1){
                if(args[0].equals("pos1")) {
                    BlockPosition pos1 = new BlockPosition(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
                    region.setPosition1(pos1);
                    messageService.sendMessage(player, MessageType.GOOD, "str.reward.crud.region.setPosition1", pos1.toString());
                }
                if(args[0].equals("pos2")) {
                    BlockPosition pos2 = new BlockPosition(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
                    region.setPosition2(pos2);
                    messageService.sendMessage(player, MessageType.GOOD, "str.reward.crud.region.setPosition2", pos2.toString());
                }
                regionService.setSelectedRegion(player, region);
                return;
            }
            if(args.length == 4){
                int x = Integer.parseInt(args[1]);
                int y = Integer.parseInt(args[2]);
                int z = Integer.parseInt(args[3]);
                if(args[0].equals("pos1")) {
                    BlockPosition pos1 = new BlockPosition(loc.getWorld().getName(), x,y,z);
                    region.setPosition1(pos1);
                }
                if(args[0].equals("pos2")) {
                    BlockPosition pos2 = new BlockPosition(loc.getWorld().getName(), x,y,z);
                    region.setPosition2(pos2);
                }
                regionService.setSelectedRegion(player, region);
                return;
            }

            throw new IllegalArgumentException("Wrong arguments!");
        }
    }
    private void selectByExactArgs(Player player, String[] args) throws IllegalArgumentException {
        if(args.length != 6){
            throw new IllegalArgumentException("Wrong arguments!");
        }
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int z = Integer.parseInt(args[2]);
        int x2 = Integer.parseInt(args[3]);
        int y2 = Integer.parseInt(args[4]);
        int z2 = Integer.parseInt(args[5]);
        BlockPosition pos1 = new BlockPosition(player.getWorld().getName(), x, y, z);
        BlockPosition pos2 = new BlockPosition(player.getWorld().getName(), x2, y2, z2);
        Region region = new RegionImpl(pos1, pos2);
        messageService.sendMessage(player, region.toString());
        regionService.setSelectedRegion(player, region);
    }
}
