package com.codestian.homeassistantmc.te;

import com.codestian.homeassistantmc.block.StateBlock;
import com.codestian.homeassistantmc.init.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class StateTileEntity extends TileEntity implements ITickableTileEntity {
    private String haEntityId = "";
    private String haEntityOperator = "=";
    private String haEntityStateToCheck = "on";
    private String haAttributeName = "";
    private String haAttributeOperator = "=";
    private String haAttributeValueToCheck = "";

    public StateTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public StateTileEntity() {
        this(ModTileEntities.STATE_TILE_ENTITY_TYPE.get());
    }

    public String getHaEntityId() {
        return haEntityId;
    }

    public void setHaEntityId(String haEntityId) {
        this.haEntityId = haEntityId;
    }

    public String getHaEntityStateToCheck() {
        return haEntityStateToCheck;
    }

    public void setHaEntityStateToCheck(String haEntityStateToCheck) {
        this.haEntityStateToCheck = haEntityStateToCheck;
    }

    public String getHaEntityOperator() {
        return haEntityOperator;
    }

    public void setHaEntityOperator(String haEntityOperator) {
        this.haEntityOperator = haEntityOperator;
    }

    public String getHaAttributeName() {
        return haAttributeName;
    }

    public void setHaAttributeName(String attributeName) {
        this.haAttributeName = attributeName;
    }

    public String getHaAttributeOperator() {
        return haAttributeOperator;
    }

    public void setHaAttributeOperator(String attributeOperator) {
        this.haAttributeOperator = attributeOperator;
    }

    public String getHaAttributeValueToCheck() {
        return haAttributeValueToCheck;
    }

    public void setHaAttributeValueToCheck(String attributeValueToCheck) {
        this.haAttributeValueToCheck = attributeValueToCheck;
    }

    @Override
    public void load(BlockState blockState, CompoundNBT parentNBTTagCompound) {
        super.load(blockState, parentNBTTagCompound);

        final int NBT_STRING_ID = StringNBT.valueOf("").getId();

        if (parentNBTTagCompound.contains("ha_entity_id", NBT_STRING_ID)) {
            haEntityId = parentNBTTagCompound.getString("ha_entity_id");
        }
        if (parentNBTTagCompound.contains("ha_entity_operator", NBT_STRING_ID)) {
            haEntityOperator = parentNBTTagCompound.getString("ha_entity_operator");
        }
        if (parentNBTTagCompound.contains("ha_entity_state_to_check", NBT_STRING_ID)) {
            haEntityStateToCheck = parentNBTTagCompound.getString("ha_entity_state_to_check");
        }

        if (parentNBTTagCompound.contains("ha_attribute_name", NBT_STRING_ID)) {
            haAttributeName = parentNBTTagCompound.getString("ha_attribute_name");
        }
        if (parentNBTTagCompound.contains("ha_attribute_operator", NBT_STRING_ID)) {
            haAttributeOperator = parentNBTTagCompound.getString("ha_attribute_operator");
        }
        if (parentNBTTagCompound.contains("ha_attribute_to_check", NBT_STRING_ID)) {
            haAttributeValueToCheck = parentNBTTagCompound.getString("ha_attribute_to_check");
        }
//        if (!"ha_entity_id".equals(haEntityId)) {
//            System.err.println("ha_entity_id mismatch:" + haEntityId);
//        }
    }

    @Override
    public CompoundNBT save(CompoundNBT parentNBTTagCompound) {
        super.save(parentNBTTagCompound);

        if (haEntityId != null) {
            parentNBTTagCompound.putString("ha_entity_id", haEntityId);
        }
        if (haEntityOperator != null) {
            parentNBTTagCompound.putString("ha_entity_operator", haEntityOperator);
        }
        if (haEntityStateToCheck != null) {
            parentNBTTagCompound.putString("ha_entity_state_to_check", haEntityStateToCheck);
        }

        if (haAttributeName != null) {
            parentNBTTagCompound.putString("ha_attribute_name", haAttributeName);
        }
        if (haAttributeOperator != null) {
            parentNBTTagCompound.putString("ha_attribute_operator", haAttributeOperator);
        }
        if (haAttributeValueToCheck != null) {
            parentNBTTagCompound.putString("ha_attribute_to_check", haAttributeValueToCheck);
        }
        return parentNBTTagCompound;
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            BlockState blockstate = this.getBlockState();
            Block block = blockstate.getBlock();
            if (block instanceof StateBlock) {
                StateBlock.updateSignalStrength(blockstate, this.level, this.worldPosition);
            }
        }
    }
}
