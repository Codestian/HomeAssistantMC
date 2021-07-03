package com.codestian.homeassistantmc.network.message;

import com.codestian.homeassistantmc.te.StateTileEntity;
import com.codestian.homeassistantmc.worldsaveddata.EntityData;
import com.google.gson.JsonElement;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateStateBlockData {

    private final String entityId;
    private final String entityOperator;
    private final String entityStateToCheck;

    private final String attributeName;
    private final String attributeOperator;
    private final String attributeValueToCheck;

    private final BlockPos blockPos;

    public UpdateStateBlockData(
            String entityId,
            String entityOperator,
            String entityStateToCheck,
            String attributeName,
            String attributeOperator,
            String attributeValueToCheck,
            BlockPos blockPos
    ) {
        this.entityId = entityId;
        this.entityOperator = entityOperator;
        this.entityStateToCheck = entityStateToCheck;
        this.attributeName = attributeName;
        this.attributeOperator = attributeOperator;
        this.attributeValueToCheck = attributeValueToCheck;
        this.blockPos = blockPos;
    }

    public static void encode(UpdateStateBlockData message, PacketBuffer buffer) {
        buffer.writeUtf(message.entityId);
        buffer.writeUtf(message.entityOperator);
        buffer.writeUtf(message.entityStateToCheck);
        buffer.writeUtf(message.attributeName);
        buffer.writeUtf(message.attributeOperator);
        buffer.writeUtf(message.attributeValueToCheck);
        buffer.writeBlockPos(message.blockPos);
    }

    public static UpdateStateBlockData decode(PacketBuffer buffer) {
        return new UpdateStateBlockData(
                buffer.readUtf(1024),
                buffer.readUtf(1024),
                buffer.readUtf(1024),
                buffer.readUtf(1024),
                buffer.readUtf(1024),
                buffer.readUtf(1024),
                buffer.readBlockPos()
        );
    }

    public static void handle(UpdateStateBlockData message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {

            ServerPlayerEntity player = context.getSender();
            ServerWorld world = player.getLevel();

            TileEntity tileEntity = world.getBlockEntity(message.blockPos);
            if (tileEntity instanceof StateTileEntity) {
                StateTileEntity stateTileEntity = (StateTileEntity) tileEntity;
                EntityData entityData = EntityData.get(world);

                boolean isEntityExist = false;

                for (JsonElement entity : entityData.getEntities()) {
                    if (entity.getAsJsonObject().get("entity_id").getAsString().equals(message.entityId)) {
                        isEntityExist = true;
                        break;
                    }
                }

                if (isEntityExist) {
                    stateTileEntity.setHaEntityId(message.entityId);
                    stateTileEntity.setHaEntityOperator(message.entityOperator);
                    stateTileEntity.setHaEntityStateToCheck(message.entityStateToCheck);
                    stateTileEntity.setHaAttributeName(message.attributeName);
                    stateTileEntity.setHaAttributeOperator(message.attributeOperator);
                    stateTileEntity.setHaAttributeValueToCheck(message.attributeValueToCheck);
                    player.sendMessage(new StringTextComponent("Entity set: " + message.entityId),
                            player.getUUID());
                } else {
                    if (message.entityId.isEmpty()) {
                        player.sendMessage(new StringTextComponent("Please input an entity first"),
                                player.getUUID());
                    } else {
                        player.sendMessage(new StringTextComponent("Entity " + message.entityId + " does not exist"),
                                player.getUUID());
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
