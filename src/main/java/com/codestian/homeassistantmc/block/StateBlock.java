package com.codestian.homeassistantmc.block;

import com.codestian.homeassistantmc.init.ModTileEntities;
import com.codestian.homeassistantmc.network.Network;
import com.codestian.homeassistantmc.network.message.OpenStateScreen;
import com.codestian.homeassistantmc.te.StateTileEntity;
import com.codestian.homeassistantmc.worldsaveddata.EntityData;
import com.google.gson.JsonObject;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class StateBlock extends Block {
    public static final IntegerProperty POWER = BlockStateProperties.POWER;
    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;

    private static String entityState = "";
    private static String attributeValue = "";

    public StateBlock() {
        super(AbstractBlock.Properties.of(Material.METAL)
                .strength(1.5F)
                .sound(SoundType.METAL));
        this.registerDefaultState(this.stateDefinition.any().setValue(POWER, 0).setValue(INVERTED, Boolean.FALSE));
    }

    public static void updateSignalStrength(BlockState blockState, World world, BlockPos blockPos) {
        TileEntity tileEntity = world.getBlockEntity(blockPos);
        if (tileEntity instanceof StateTileEntity) {
            EntityData entityData = EntityData.get(world);
            StateTileEntity stateTileEntity = (StateTileEntity) tileEntity;
            if (entityData.getEntities().size() != 0) {
                try {
                    JsonObject entity = entityData.getOneEntity(stateTileEntity.getHaEntityId());

                    boolean entityCondition = false;
                    boolean attributeCondition = false;

                    if (!stateTileEntity.getHaEntityId().isEmpty() && !stateTileEntity.getHaEntityStateToCheck().isEmpty()) {
                        String state = entity.get("state").getAsString();
                        entityState = state;
                        try {
                            switch (stateTileEntity.getHaEntityOperator()) {
                                case "=":
                                    if (state.equals(stateTileEntity.getHaEntityStateToCheck()) || Double.parseDouble(state) == Double.parseDouble(stateTileEntity.getHaEntityStateToCheck())) {
                                        entityCondition = true;
                                    }
                                    break;
                                case "<":
                                    if (Double.parseDouble(state) < Double.parseDouble(stateTileEntity.getHaEntityStateToCheck())) {
                                        entityCondition = true;
                                    }
                                    break;
                                case ">":
                                    if (Double.parseDouble(state) > Double.parseDouble(stateTileEntity.getHaEntityStateToCheck())) {
                                        entityCondition = true;
                                    }
                                    break;
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }

                    if (!stateTileEntity.getHaAttributeName().isEmpty() && !stateTileEntity.getHaAttributeValueToCheck().isEmpty()) {
                        if (entity.get("attributes").getAsJsonObject().has(stateTileEntity.getHaAttributeName())) {

                            String attribute = entity.get("attributes").getAsJsonObject().get(stateTileEntity.getHaAttributeName()).toString();
                            attributeValue = attribute;

                            try {
                                switch (stateTileEntity.getHaAttributeOperator()) {
                                    case "=":
                                        if (attribute.equals(stateTileEntity.getHaAttributeValueToCheck()) || Double.parseDouble(attribute) == Double.parseDouble(stateTileEntity.getHaAttributeValueToCheck())) {
                                            attributeCondition = true;
                                        }
                                        break;
                                    case "<":
                                        if (Double.parseDouble(attribute) < Double.parseDouble(stateTileEntity.getHaAttributeValueToCheck())) {
                                            attributeCondition = true;
                                        }
                                        break;
                                    case ">":
                                        if (Double.parseDouble(attribute) > Double.parseDouble(stateTileEntity.getHaAttributeValueToCheck())) {
                                            attributeCondition = true;
                                        }
                                        break;
                                }
                            } catch (NumberFormatException ignored) {
                            }
                        } else {
//                        Put initial values for attributes that don't exist in current entity object
                            if (stateTileEntity.getHaAttributeName().equals("brightness")) {
                                attributeValue = "0";
                            } else if (stateTileEntity.getHaAttributeName().equals("rgb_color")) {
                                attributeValue = "[0.0,0.0,0.0]";
                            }
                        }
                    } else {
                        attributeCondition = true;
                    }

                    if (entityCondition && attributeCondition) {
                        world.setBlock(blockPos, blockState.setValue(POWER, 15).setValue(INVERTED, true), 3);
                    } else {
                        world.setBlock(blockPos, blockState.setValue(POWER, 0).setValue(INVERTED, false), 3);
                    }
                } catch (Exception ignored) {
                }

            }
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.STATE_TILE_ENTITY_TYPE.get().create();
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState blockstate, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult p_225533_6_) {
        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            TileEntity tileEntity = world.getBlockEntity(blockPos);
            if (tileEntity instanceof StateTileEntity) {
                StateTileEntity stateTileEntity = (StateTileEntity) tileEntity;
                Network.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                        new OpenStateScreen(
                                stateTileEntity.getHaEntityId(),
                                stateTileEntity.getHaEntityOperator(),
                                stateTileEntity.getHaEntityStateToCheck(),
                                stateTileEntity.getHaAttributeName(),
                                stateTileEntity.getHaAttributeOperator(),
                                stateTileEntity.getHaAttributeValueToCheck(),
                                entityState,
                                attributeValue,
                                blockPos)
                );
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
            return ActionResultType.CONSUME;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isSignalSource(BlockState blockstate) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getSignal(BlockState blockstate, IBlockReader blockReader, BlockPos pos, Direction direction) {
        return blockstate.getValue(POWER);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(POWER, INVERTED);
    }
}
