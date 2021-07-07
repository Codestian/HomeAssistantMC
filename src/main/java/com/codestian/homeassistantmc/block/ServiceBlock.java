package com.codestian.homeassistantmc.block;

import com.codestian.homeassistantmc.init.ModTileEntities;
import com.codestian.homeassistantmc.network.Network;
import com.codestian.homeassistantmc.network.message.OpenServiceScreen;
import com.codestian.homeassistantmc.te.ServiceTileEntity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.Random;

import static com.codestian.homeassistantmc.event.ServerEvent.webSocket;

public class ServiceBlock extends Block {

    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
    private final Gson gson = new Gson();

    public ServiceBlock() {
        super(AbstractBlock.Properties.of(Material.METAL)
                .strength(1.5F)
                .sound(SoundType.METAL));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.SERVICE_TILE_ENTITY_TYPE.get().create();
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState blockstate, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult p_225533_6_) {
        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            TileEntity tileEntity = world.getBlockEntity(blockPos);
            if (tileEntity instanceof ServiceTileEntity) {
                Network.CHANNEL.send(PacketDistributor.PLAYER.with(
                        () -> (ServerPlayerEntity) player),
                        new OpenServiceScreen(((ServiceTileEntity) tileEntity).getHaServiceConfig(), blockPos)
                );
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
            return ActionResultType.CONSUME;
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        return this.defaultBlockState().setValue(LIT, p_196258_1_.getLevel().hasNeighborSignal(p_196258_1_.getClickedPos()));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState blockState, World world, BlockPos blockPos, Block block, BlockPos p_220069_5_, boolean p_220069_6_) {
        if (!world.isClientSide) {
            boolean flag = blockState.getValue(LIT);
            if (flag != world.hasNeighborSignal(blockPos)) {
                if (flag) {
                    world.getBlockTicks().scheduleTick(blockPos, this, 4);
                } else {
                    world.setBlock(blockPos, blockState.cycle(LIT), 2);
                    if (world.hasNeighborSignal(blockPos)) {
                        TileEntity tileEntity = world.getBlockEntity(blockPos);
                        if (tileEntity instanceof ServiceTileEntity) {
                            ServiceTileEntity stateTileEntity = (ServiceTileEntity) tileEntity;
                            try {
                                JsonObject jsonObject = gson.fromJson(stateTileEntity.getHaServiceConfig(), JsonObject.class);
                                String[] service = jsonObject.get("service").getAsString().split("\\.");
                                JsonObject serviceData = new JsonObject();
                                JsonObject serviceTarget = new JsonObject();

                                if (jsonObject.has("data")) {
                                    serviceData = jsonObject.get("data").getAsJsonObject();
                                }
                                if (jsonObject.has("target")) {
                                    serviceTarget = jsonObject.get("target").getAsJsonObject();
                                }
                                webSocket.callService(service[0], service[1], serviceData, serviceTarget);
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
            }


        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void tick(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos, Random p_225534_4_) {
        if (blockState.getValue(LIT) && !serverWorld.hasNeighborSignal(blockPos)) {
            serverWorld.setBlock(blockPos, blockState.cycle(LIT), 2);
        }

    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(LIT);
    }
}
