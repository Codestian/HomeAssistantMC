package com.codestian.homeassistantmc;

import com.codestian.homeassistantmc.config.Config;
import com.codestian.homeassistantmc.init.ModBlocks;
import com.codestian.homeassistantmc.init.ModItems;
import com.codestian.homeassistantmc.init.ModTileEntities;
import com.codestian.homeassistantmc.network.Network;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("homeassistantmc")
public class HomeAssistantMC {

    public static final Logger logger = LogManager.getLogger();
    public static final String MOD_ID = "homeassistantmc";

    public HomeAssistantMC() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.common_config);

        Config.loadConfig(Config.common_config, FMLPaths.CONFIGDIR.get().resolve("homeassistantmc-common.toml").toString());

        bus.addListener(this::commonSetup);
        ModBlocks.BLOCKS.register(bus);
        ModTileEntities.TILE_ENTITY_TYPE.register(bus);
        ModItems.ITEMS.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        Network.init();
    }
}
