package ru.strid.strreward.schedulers;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.strid.strreward.entities.BlockPosition;
import ru.strid.strreward.entities.Region;
import ru.strid.strreward.services.RegionService;

import java.util.Map;

public class RegionSelectionParticleShower extends BukkitRunnable {

    private final RegionService regionService = RegionService.getInstance();
    private final double STEP = 0.1;

    @Override
    public void run() {
        drawRegionsForEachPlayer();
    }

    private void drawRegionsForEachPlayer(){
        for(Map.Entry<Player, Region> entry : regionService.getAllSelectedRegions().entrySet()){
            if(entry.getValue().isValid()){
                highlight(entry.getKey(), entry.getValue());
            }
        }
    }



    private void highlight(Player player, Region region) {
        BlockPosition pos1 = region.getPosition1();
        BlockPosition pos2 = region.getPosition2();

        World world = Bukkit.getWorld(pos1.getWorldName());
        if (world == null) return;

        double minX = Math.min(pos1.getX(), pos2.getX());
        double minY = Math.min(pos1.getY(), pos2.getY());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxX = Math.max(pos1.getX(), pos2.getX());
        double maxY = Math.max(pos1.getY(), pos2.getY());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());
        maxX = maxX + 1L;
        maxY = maxY + 1L;
        maxZ = maxZ + 1L;

        for (double x = minX; x <= maxX; x += STEP) {
            spawn(player, x, minY, minZ);
            spawn(player, x, minY, maxZ);
            spawn(player, x, maxY, minZ);
            spawn(player, x, maxY, maxZ);
        }

        for (double y = minY; y <= maxY; y += STEP) {
            spawn(player, minX, y, minZ);
            spawn(player, minX, y, maxZ);
            spawn(player, maxX, y, minZ);
            spawn(player, maxX, y, maxZ);
        }

        for (double z = minZ; z <= maxZ; z += STEP) {
            spawn(player, minX, minY, z);
            spawn(player, minX, maxY, z);
            spawn(player, maxX, minY, z);
            spawn(player, maxX, maxY, z);
        }

    }

    private void spawn(Player player, double x, double y, double z) {
        Location loc = new Location(player.getWorld(), x, y, z);
        player.spawnParticle(
                Particle.DUST,
                loc,
                1,
                0, 0, 0,
                0,
                new Particle.DustOptions(Color.fromRGB(0, 255, 255), 1.0f)
        );
    }

    //TODO Сделать систему выделения ВСЕХ rewardZone и с автоматической системы подборов контрастных цветов
    // для визуального отделения нескольких наслаивающийхся зон друг от друга


}
