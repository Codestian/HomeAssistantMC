package com.codestian.homeassistantmc.init;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.codestian.homeassistantmc.HomeAssistantMC.MOD_ID;

public class ModItems {

    //The ITEMS deferred register in which you can register items.
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    //Register the tutorial block's item so a player can place it.
    public static final RegistryObject<Item> STATE_BLOCK = ITEMS.register("state_block", () -> new BlockItem(ModBlocks.STATE_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
    public static final RegistryObject<Item> SERVICE_BLOCK = ITEMS.register("service_block", () -> new BlockItem(ModBlocks.SERVCE_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));

}
