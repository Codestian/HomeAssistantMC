package com.codestian.homeassistantmc.commands;

import com.codestian.homeassistantmc.worldsaveddata.EntityData;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.codestian.homeassistantmc.event.ServerEvent.webSocket;

public class Command {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("hass")
                .then(Commands.literal("list")
                        .then(Commands.argument("index", IntegerArgumentType.integer())
                                .executes(Command::listAllEntities)
                        )
                )
                .then(Commands.literal("status")
                        .executes(Command::getWebSocketStatus)
                )
        );
    }

    private static int getWebSocketStatus(CommandContext<CommandSource> commandContext) {
        Entity player = commandContext.getSource().getEntity();
        if (player instanceof ServerPlayerEntity) {
            player.sendMessage(new StringTextComponent("[HomeAssistantMC] " + webSocket.getStatus()), player.getUUID());
        }
        return 1;
    }

    private static int listAllEntities(CommandContext<CommandSource> commandContext) throws CommandSyntaxException {

        int index = IntegerArgumentType.getInteger(commandContext, "index") - 1;
        World world = commandContext.getSource().getLevel();
        Entity player = commandContext.getSource().getEntity();
        if (!world.isClientSide() && player instanceof ServerPlayerEntity) {
            EntityData entityData = EntityData.get(world);
            JsonArray entities = entityData.getEntities();
            List<String> entitiesList = new ArrayList<>();
            for (JsonElement entity : entities) {
                entitiesList.add(entity.getAsJsonObject().get("entity_id").getAsString());
            }
            Collections.sort(entitiesList);

            List<String> paginatedEntitiesList = entitiesList.subList(10 * index, 10 * index + 10);
            paginatedEntitiesList.add(0, "<Showing list of entities " + ((10 * index) + 1) + " - " + ((10 * index) + 10) + ">");
            player.sendMessage(new StringTextComponent(String.join("\n", paginatedEntitiesList)), player.getUUID());
        }

        return 1;
    }

}
