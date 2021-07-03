package com.codestian.homeassistantmc.network.message;

import com.codestian.homeassistantmc.client.gui.StateScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class OpenStateHandler {
    public static void handlePacket(
            String entityId,
            String entityOperator,
            String entityStateToCheck,
            String attributeName,
            String attributeOperator,
            String attributeValueToCheck,
            String currentEntityState,
            String currentAttributeValue,
            BlockPos blockPos) {
        ITextComponent test = new TranslationTextComponent("screen.forgepractice.service_block");
        Minecraft.getInstance().setScreen(new StateScreen(
                test,
                entityId,
                entityOperator,
                entityStateToCheck,
                attributeName,
                attributeOperator,
                attributeValueToCheck,
                currentEntityState,
                currentAttributeValue,
                blockPos));
    }
}