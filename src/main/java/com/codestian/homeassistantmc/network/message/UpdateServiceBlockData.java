package com.codestian.homeassistantmc.network.message;

import com.codestian.homeassistantmc.te.ServiceTileEntity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateServiceBlockData {
    private final String entityConfig;
    private final BlockPos blockPos;

    public UpdateServiceBlockData(String entityConfig, BlockPos blockPos) {
        this.entityConfig = entityConfig;
        this.blockPos = blockPos;
    }

    public static void encode(UpdateServiceBlockData message, PacketBuffer buffer) {
        buffer.writeUtf(message.entityConfig);
        buffer.writeBlockPos(message.blockPos);
    }

    public static UpdateServiceBlockData decode(PacketBuffer buffer) {
        return new UpdateServiceBlockData(buffer.readUtf(32767), buffer.readBlockPos());
    }

    public static void handle(UpdateServiceBlockData message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            ServerWorld world = player.getLevel();
            TileEntity tileEntity = world.getBlockEntity(message.blockPos);
            if (tileEntity instanceof ServiceTileEntity) {
                ServiceTileEntity serviceTileEntity = (ServiceTileEntity) tileEntity;
                Gson gson = new Gson();
                try {
                    gson.fromJson(message.entityConfig, JsonObject.class);
                    serviceTileEntity.setHaServiceConfig(message.entityConfig);
                    player.sendMessage(new StringTextComponent("Service configuration set"), player.getUUID());
                } catch (JsonSyntaxException e) {
                    player.sendMessage(new StringTextComponent("Service configuration is not set"), player.getUUID());
                }
            }
        });
        context.setPacketHandled(true);
    }
}
