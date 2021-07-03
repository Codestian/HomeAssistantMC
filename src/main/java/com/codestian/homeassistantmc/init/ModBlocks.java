package com.codestian.homeassistantmc.init;

import com.codestian.homeassistantmc.block.ServiceBlock;
import com.codestian.homeassistantmc.block.StateBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.codestian.homeassistantmc.HomeAssistantMC.MOD_ID;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    public static final RegistryObject<Block> STATE_BLOCK = BLOCKS.register("state_block", StateBlock::new);
    public static final RegistryObject<Block> SERVCE_BLOCK = BLOCKS.register("service_block", ServiceBlock::new);

}