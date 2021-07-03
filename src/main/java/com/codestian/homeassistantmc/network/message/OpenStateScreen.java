package com.codestian.homeassistantmc.network.message;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenStateScreen {
    private final String entityId;
    private final String entityOperator;
    private final String entityStateToCheck;

    private final String attributeName;
    private final String attributeOperator;
    private final String attributeValueToCheck;

    private final String currentEntityState;
    private final String currentAttributeValue;

    private final BlockPos blockPos;

    public OpenStateScreen(
            String entityId,
            String entityOperator,
            String entityStateToCheck,
            String attributeName,
            String attributeOperator,
            String attributeValueToCheck,
            String currentEntityState,
            String currentAttributeValue,
            BlockPos blockPos
    ) {
        this.entityId = entityId;
        this.entityOperator = entityOperator;
        this.entityStateToCheck = entityStateToCheck;

        this.attributeName = attributeName;
        this.attributeOperator = attributeOperator;
        this.attributeValueToCheck = attributeValueToCheck;

        this.currentEntityState = currentEntityState;
        this.currentAttributeValue = currentAttributeValue;

        this.blockPos = blockPos;
    }

    public static void encode(OpenStateScreen message, PacketBuffer buffer) {
        buffer.writeUtf(message.entityId);
        buffer.writeUtf(message.entityOperator);
        buffer.writeUtf(message.entityStateToCheck);

        buffer.writeUtf(message.attributeName);
        buffer.writeUtf(message.attributeOperator);
        buffer.writeUtf(message.attributeValueToCheck);

        buffer.writeUtf(message.currentEntityState);
        buffer.writeUtf(message.currentAttributeValue);

        buffer.writeBlockPos(message.blockPos);
    }

    public static OpenStateScreen decode(PacketBuffer buffer) {
        return new OpenStateScreen(
                buffer.readUtf(512),
                buffer.readUtf(1),
                buffer.readUtf(512),
                buffer.readUtf(512),
                buffer.readUtf(1),
                buffer.readUtf(512),
                buffer.readUtf(512),
                buffer.readUtf(512),
                buffer.readBlockPos());
    }

    public static void handle(OpenStateScreen message, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> OpenStateHandler.handlePacket(message.entityId,
                        message.entityOperator,
                        message.entityStateToCheck,
                        message.attributeName,
                        message.attributeOperator,
                        message.attributeValueToCheck,
                        message.currentEntityState,
                        message.currentAttributeValue, message.blockPos))
        );
        contextSupplier.get().setPacketHandled(true);
    }
}
