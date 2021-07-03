package com.codestian.homeassistantmc.network.message;

import com.codestian.homeassistantmc.client.gui.ServiceScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class OpenServiceHandler {
    public static void handlePacket(String entityConfig, BlockPos blockPos) {
        ITextComponent test = new TranslationTextComponent("screen.forgepractice.service_block");
        Minecraft.getInstance().setScreen(new ServiceScreen(test, entityConfig, blockPos));
    }
}
