package com.codestian.homeassistantmc.network;

import com.codestian.homeassistantmc.network.message.OpenServiceScreen;
import com.codestian.homeassistantmc.network.message.OpenStateScreen;
import com.codestian.homeassistantmc.network.message.UpdateServiceBlockData;
import com.codestian.homeassistantmc.network.message.UpdateStateBlockData;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import static com.codestian.homeassistantmc.HomeAssistantMC.MOD_ID;

public class Network {

    public static final String NETWORK_VERSION = "0.2.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MOD_ID, "network"), () -> NETWORK_VERSION,
            version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));

    public static void init() {
        CHANNEL.registerMessage(0, UpdateStateBlockData.class, UpdateStateBlockData::encode, UpdateStateBlockData::decode, UpdateStateBlockData::handle);
        CHANNEL.registerMessage(1, UpdateServiceBlockData.class, UpdateServiceBlockData::encode, UpdateServiceBlockData::decode, UpdateServiceBlockData::handle);
        CHANNEL.registerMessage(2, OpenStateScreen.class, OpenStateScreen::encode, OpenStateScreen::decode, OpenStateScreen::handle);
        CHANNEL.registerMessage(3, OpenServiceScreen.class, OpenServiceScreen::encode, OpenServiceScreen::decode, OpenServiceScreen::handle);
    }

}
