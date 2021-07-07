package com.codestian.homeassistantmc.init;

import com.codestian.homeassistantmc.te.ServiceTileEntity;
import com.codestian.homeassistantmc.te.StateTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.codestian.homeassistantmc.HomeAssistantMC.MOD_ID;

public class ModTileEntities {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MOD_ID);

    public static final RegistryObject<TileEntityType<StateTileEntity>> STATE_TILE_ENTITY_TYPE = TILE_ENTITY_TYPE
            .register("state", () -> TileEntityType.Builder.of(StateTileEntity::new, ModBlocks.STATE_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<ServiceTileEntity>> SERVICE_TILE_ENTITY_TYPE = TILE_ENTITY_TYPE
            .register("service", () -> TileEntityType.Builder.of(ServiceTileEntity::new, ModBlocks.SERVCE_BLOCK.get()).build(null));

}
