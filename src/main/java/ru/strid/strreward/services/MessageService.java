package ru.strid.strreward.services;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.strid.strreward.STRrewardZone;
import ru.strid.strreward.enums.MessageType;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageService {

    private final STRrewardZone plugin;
    private final Logger logger = STRrewardZone.getInstance().getLogger();
    private static final MessageService MESSAGE_SERVICE = new MessageService();
    private static final String PREFIX = "§7[§bSTR§er§az§7]: §r";

    private static final String DEFAULT_LOCALE = "en";

    private Map<String, FileConfiguration> languages = new HashMap<>();

    //Хешмапа <String, FileConfiguration> Локаль(ключ), конфиг значение

    //Метод гетПереводыПоКлючуЛокалиИзХешмапы

    private void initLanguages(){
        File folder = new File(plugin.getDataFolder() + File.separator + plugin.LOCALIZATION_FOLDER_NAME);
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            logger.log(Level.WARNING, "No language files found!");
            return;
        }
        for(File file : files){
            try {
                String locale = file.getName().split("_")[0];
                if(file.getName().endsWith(".yml")){
                    FileConfiguration lang = YamlConfiguration.loadConfiguration(file);
                    languages.put(locale, lang);
                }
            } catch (Exception e){
                logger.warning("Failed to load language file " + file.getName());
                logger.warning(e.getMessage());
            }

        }

        File ru_file = new File(plugin.getDataFolder() + File.separator + plugin.LOCALIZATION_FOLDER_NAME + File.separator + "ru_lang.yml");
        FileConfiguration ru_lang = YamlConfiguration.loadConfiguration(ru_file);
    }


    private MessageService() {
        this.plugin = STRrewardZone.getInstance();
        //init();
        initLanguages();
    }


    public static MessageService getMessageService() {
        return MESSAGE_SERVICE;
    }

    public void sendMessage(CommandSender target, MessageType type, String... messageCodes){
        StringBuilder msgOutput = new StringBuilder();
        for (int i = 0; i < messageCodes.length; i++) {
            msgOutput.append(getLocalizedMessage(getLocale(target), messageCodes[i]));
            if (i < messageCodes.length - 1) {
                msgOutput.append(" ");
            }
        }
        msgOutput.insert(0, PREFIX + type.getColor());
        target.sendMessage(msgOutput.toString());
    }

    public void sendMessage(CommandSender target, String... messageCode){
        sendMessage(target, MessageType.INFO, messageCode);
    }

    private String getLocalizedMessage(String locale, String messageCode){
        if(!languages.containsKey(locale)){
            return getMessageFromFile(languages.get(DEFAULT_LOCALE), messageCode);
        }
        return getMessageFromFile(languages.get(locale), messageCode);
    }

    private String getMessageFromFile(FileConfiguration langFile, String messageCode){
        try {
            //Проверяем есть ли данный код в выбранной локализации
            if(!langFile.contains(messageCode)){
                //Проверяем есть ли данный код в английской локализации
                if(!languages.get(DEFAULT_LOCALE).contains(messageCode)){
                    //Если ничего не нашлось возрвщаем сам код
                    return messageCode;
                }
                //Если нет в выбранной локализации, то используем сообщение из аглийской
                return languages.get(DEFAULT_LOCALE).getString(messageCode);
            }
            //Возвращаем сообщение из выбранной локализации
            return langFile.getString(messageCode);
        } catch (Exception e){
            plugin.getLogger().log(Level.SEVERE, "Localization file is damaged!", e);
            return messageCode;
        }
    }

    //TODO Верунть private
    private String getLocale(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return "en";
        }
        try {
            Player player = (Player) sender;
            String locale = player.getLocale();
            return locale.split("_")[0];
        } catch (Exception e) {
            return "en";
        }
    }
}
