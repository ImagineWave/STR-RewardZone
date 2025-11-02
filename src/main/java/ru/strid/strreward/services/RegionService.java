package ru.strid.strreward.services;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.strid.strreward.STRrewardZone;
import ru.strid.strreward.entities.Region;
import ru.strid.strreward.entities.Reward;
import ru.strid.strreward.entities.impl.RegionImpl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegionService {
    private final STRrewardZone plugin;
    private static final RegionService INSTANCE = new RegionService();

    private final Map<Player, Region> selectedRegions = new HashMap<>();


    private RegionService() {
        plugin = STRrewardZone.getInstance();

    }

    public static RegionService getInstance() {
        return INSTANCE;
    }

    public Region getSelectedRegion(Player player) {
        return selectedRegions.get(player);
    }

    public Map<Player, Region> getAllSelectedRegions() {
        return selectedRegions;
    }

    public void setSelectedRegion(Player player, Region region) {
        selectedRegions.put(player, region);
    }

    public void removeSelectedRegion(Player player) {
        selectedRegions.remove(player);
    }



}
