package com.codestian.homeassistantmc.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class WebSocketConfig {
    public static ForgeConfigSpec.ConfigValue<String> url;
    public static ForgeConfigSpec.ConfigValue<String> token;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.comment("WebSocket Config");

        token = builder.comment("Long lived access token from Home Assistant").define("ha_token", "");
        url = builder.comment("The URL of the Home Assistant instance").define("ha_url", "");

    }
}
