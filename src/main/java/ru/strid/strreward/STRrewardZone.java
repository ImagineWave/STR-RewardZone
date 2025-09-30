package ru.strid.strreward;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.strid.strreward.commands.RegionSelectCommand;
import ru.strid.strreward.commands.RewardCRUDCommand;
import ru.strid.strreward.listeners.TestListener;

import java.io.File;
import java.util.logging.Logger;

public class STRrewardZone extends JavaPlugin {

    private static STRrewardZone INSTANCE;
    private final Logger logger = getLogger();
    public final String LOCALIZATION_FOLDER_NAME = "Localizations";

    private STRrewardZone(){}

    public static STRrewardZone getInstance(){
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        logger.info("Enabling STRrewardZone!");
        super.onEnable();

        initLocalizations();
        onStart();
    }

    @Override
    public void onDisable() {
        logger.info("Disabling STRrewardZone");
        super.onDisable();
    }

    private void onStart() {
        getCommand("rewardzone").setExecutor(new RewardCRUDCommand());
        getCommand("rewardregion").setExecutor(new RegionSelectCommand());

        Bukkit.getServer().getPluginManager().registerEvents(new TestListener(), this);
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