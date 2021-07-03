package com.codestian.homeassistantmc.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.io.File;

import static com.codestian.homeassistantmc.HomeAssistantMC.logger;

@Mod.EventBusSubscriber
public class Config {

    public static final ForgeConfigSpec common_config;
    private static final ForgeConfigSpec.Builder common_builder = new ForgeConfigSpec.Builder();

    static {
        WebSocketConfig.init(common_builder);
        common_config = common_builder.build();
    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        logger.info("Loading config:" + path);
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        logger.info("Built config" + path);
        file.load();
        logger.info("Loaded config " + path);
        config.setConfig(file);
    }
}
