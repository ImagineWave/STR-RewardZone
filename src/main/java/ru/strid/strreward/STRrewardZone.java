package ru.strid.strreward;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import ru.strid.strreward.commands.LoadZonesCommand;
import ru.strid.strreward.commands.RegionSelectCommand;
import ru.strid.strreward.commands.RewardCRUDCommand;
import ru.strid.strreward.entities.*;
import ru.strid.strreward.listeners.RewardZoneListener;
import ru.strid.strreward.schedulers.RegionSelectionParticleShower;
import ru.strid.strreward.services.RegionService;
import ru.strid.strreward.services.RewardService;
import ru.strid.strreward.services.RewardZoneService;

import java.io.File;
import java.util.logging.Logger;

public class STRrewardZone extends JavaPlugin {

    private static STRrewardZone INSTANCE;
    private final Logger logger = getLogger();
    public final String LOCALIZATION_FOLDER_NAME = "Localizations";
    public final String SAVED_DATA_FOLDER_NAME = "Data";

    private STRrewardZone(){}

    public static STRrewardZone getInstance(){
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        super.onEnable();

        registerEntities();
        registerEvents();
        initLocalizations();
        initServices();
        onStart();
    }

    @Override
    public void onDisable() {
        logger.info("Disabling STRrewardZone");
        RewardService.getInstance().saveRewards();
        RewardZoneService.getInstance().saveRewardZones();
        super.onDisable();
    }

    private void onStart() {
        getCommand("rewardzone").setExecutor(new RewardCRUDCommand());
        getCommand("rewardregion").setExecutor(new RegionSelectCommand());
        getCommand("rewardload").setExecutor(new LoadZonesCommand());

        //Bukkit.getServer().getPluginManager().registerEvents(new TestListener(), this);

        BukkitTask globalCheck = new RegionSelectionParticleShower().runTaskTimer(this, 0, 10L); //TODO
    }
    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new RewardZoneListener(), this);
    }

    private void initServices(){
        RegionService.getInstance();
        RewardService.getInstance();
        RewardZoneService.getInstance();
    }

    private void registerEntities(){
        ConfigurationSerialization.registerClass(RewardZone.class, "RewardZone");
        ConfigurationSerialization.registerClass(Reward.class, "Reward");
        ConfigurationSerialization.registerClass(RewardContent.class, "RewardContent");
        ConfigurationSerialization.registerClass(RewardedPlayer.class, "RewardedPlayer");
        ConfigurationSerialization.registerClass(Cooldown.class, "Cooldown");
    }

    private void initLocalizations() {
        File ru_file = new File(this.getDataFolder() + File.separator + this.LOCALIZATION_FOLDER_NAME + File.separator + "ru_lang.yml");
        File en_file = new File(this.getDataFolder() + File.separator + this.LOCALIZATION_FOLDER_NAME + File.separator + "en_lang.yml");
        if (!en_file.exists()) {
            saveResource("Localizations/en_lang.yml", true);
        }
        if (!ru_file.exists()) {
            saveResource("Localizations/ru_lang.yml", true);
        }
    }
}