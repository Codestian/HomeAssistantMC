package com.codestian.homeassistantmc.te;

import com.codestian.homeassistantmc.init.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class ServiceTileEntity extends TileEntity implements ITickableTileEntity {
    private String haServiceConfig = "";

    public ServiceTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public ServiceTileEntity() {
        this(ModTileEntities.SERVICE_TILE_ENTITY_TYPE.get());
    }

    public String getHaServiceConfig() {
        return haServiceConfig;
    }

    public void setHaServiceConfig(String haServiceConfig) {
        this.haServiceConfig = haServiceConfig;
    }

    @Override
    public void load(BlockState blockState, CompoundNBT parentNBTTagCompound) {
        super.load(blockState, parentNBTTagCompound);

        String readTestString1 = null;
        final int NBT_STRING_ID = StringNBT.valueOf("").getId();

        if (parentNBTTagCompound.contains("ha_service_config", NBT_STRING_ID)) {
            haServiceConfig = parentNBTTagCompound.getString("ha_service_config");
        }

    }

    @Override
    public CompoundNBT save(CompoundNBT parentNBTTagCompound) {
        super.save(parentNBTTagCompound);
        if (haServiceConfig != null) {
            parentNBTTagCompound.putString("ha_service_config", haServiceConfig);
        }
        return parentNBTTagCompound;
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            BlockState blockstate = this.getBlockState();
            Block block = blockstate.getBlock();
//            if (block instanceof StateBlock) {
//            }
        }
    }
}
