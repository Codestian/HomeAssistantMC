package com.codestian.homeassistantmc.network.message;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenServiceScreen {
    private final String entityConfig;
    private final BlockPos blockPos;

    public OpenServiceScreen(String entityConfig, BlockPos blockPos) {
        this.entityConfig = entityConfig;
        this.blockPos = blockPos;
    }

    public static void encode(OpenServiceScreen message, PacketBuffer buffer) {
        buffer.writeUtf(message.entityConfig);
        buffer.writeBlockPos(message.blockPos);
    }

    public static OpenServiceScreen decode(PacketBuffer buffer) {
        return new OpenServiceScreen(buffer.readUtf(32767), buffer.readBlockPos());
    }

    public static void handle(OpenServiceScreen message, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> OpenServiceHandler.handlePacket(message.entityConfig, message.blockPos))
        );
        contextSupplier.get().setPacketHandled(true);
    }
}
