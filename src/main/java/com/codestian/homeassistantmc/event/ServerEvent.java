package com.codestian.homeassistantmc.event;

import com.codestian.homeassistantmc.HomeAssistantMC;
import com.codestian.homeassistantmc.commands.Command;
import com.codestian.homeassistantmc.config.WebSocketConfig;
import com.codestian.homeassistantmc.websocket.WebSocket;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import java.net.URISyntaxException;

@EventBusSubscriber(modid = HomeAssistantMC.MOD_ID, bus = Bus.FORGE)
public class ServerEvent {

    public static WebSocket webSocket = null;

    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> commandDispatcher = event.getDispatcher();
        Command.register(commandDispatcher);
    }

    @SubscribeEvent
    public static void onServerStart(FMLServerStartingEvent event) throws URISyntaxException {
        webSocket = new WebSocket(WebSocketConfig.url.get(), WebSocketConfig.token.get(), event.getServer());
        if (!WebSocketConfig.token.get().isEmpty() && !WebSocketConfig.url.get().isEmpty()) {
            webSocket.connect();
        } else {
            webSocket.setStatus("Please configure url and token");
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        event.getPlayer().sendMessage(new StringTextComponent("[HomeAssistantMC] " + webSocket.getStatus()), event.getPlayer().getUUID());
    }

}
