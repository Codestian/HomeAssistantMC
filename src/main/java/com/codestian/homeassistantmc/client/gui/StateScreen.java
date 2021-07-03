package com.codestian.homeassistantmc.client.gui;

import com.codestian.homeassistantmc.network.Network;
import com.codestian.homeassistantmc.network.message.UpdateStateBlockData;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StateScreen extends Screen {

    private final String entityId;
    private final String entityOperator;
    private final String entityStateToCheck;
    private final String attributeName;
    private final String attributeOperator;
    private final String attributeValueToCheck;
    private final String currentEntityState;
    private final String currentAttributeValue;
    private final BlockPos blockPos;
    protected Button doneButton;
    protected Button cancelButton;
    protected Button toggleEntityOperatorButton;
    protected Button toggleAttributeOperatorButton;
    protected TextFieldWidget inputEntityId;
    protected TextFieldWidget inputEntityStateToCheck;
    protected TextFieldWidget inputAttributeName;
    protected TextFieldWidget inputAttributeValueToCheck;
    protected TextFieldWidget outputEntityId;
    protected TextFieldWidget outputAttributeName;

    public StateScreen(
            ITextComponent p_i51108_1_,
            String entityId,
            String entityOperator,
            String entityStateToCheck,
            String attributeName,
            String attributeOperator,
            String attributeValueToCheck,
            String currentEntityState,
            String currentAttributeValue,
            BlockPos blockPos
    ) {
        super(p_i51108_1_);

        this.entityId = entityId;
        this.entityOperator = entityOperator;
        this.entityStateToCheck = entityStateToCheck;

        this.attributeName = attributeName;
        this.attributeOperator = attributeOperator;
        this.attributeValueToCheck = attributeValueToCheck;

        this.currentEntityState = currentEntityState;
        this.currentAttributeValue = currentAttributeValue;

        this.blockPos = blockPos;

    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);

        this.doneButton = this.addButton(new Button(this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20, DialogTexts.GUI_DONE, (p_214187_1_) -> {
            this.onDone();
        }));
        this.cancelButton = this.addButton(new Button(this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20, DialogTexts.GUI_CANCEL, (p_214186_1_) -> {
            this.onClose();
        }));

        this.toggleEntityOperatorButton = this.addButton(new Button(this.width / 2 - 10, 50, 20, 20, new TranslationTextComponent("="), (p_214187_1_) -> {
            setOperatorIcon(inputEntityStateToCheck, toggleEntityOperatorButton);
        }));
        this.toggleAttributeOperatorButton = this.addButton(new Button(this.width / 2 - 10, 90, 20, 20, new TranslationTextComponent("="), (p_214186_1_) -> {
            setOperatorIcon(inputAttributeValueToCheck, toggleAttributeOperatorButton);
        }));

        this.inputEntityId = new TextFieldWidget(this.font, this.width / 2 - 150, 50, 150 - 15, 20, new TranslationTextComponent("PLACEHOLDER")) {
        };
        this.inputEntityStateToCheck = new TextFieldWidget(this.font, this.width / 2 + 15, 50, 150 - 15, 20, new TranslationTextComponent("PLACEHOLDER")) {
        };
        this.inputAttributeName = new TextFieldWidget(this.font, this.width / 2 - 150, 90, 150 - 15, 20, new TranslationTextComponent("PLACEHOLDER")) {
        };
        this.inputAttributeValueToCheck = new TextFieldWidget(this.font, this.width / 2 + 15, 90, 150 - 15, 20, new TranslationTextComponent("PLACEHOLDER")) {
        };

        this.outputEntityId = new TextFieldWidget(this.font, this.width / 2 - 150 + 4, this.height / 4 + 120 - 12 - 10 - 20 + 6, 300, 20, new TranslationTextComponent("PLACEHOLDER")) {
        };
        this.outputAttributeName = new TextFieldWidget(this.font, this.width / 2 - 150 + 4, this.height / 4 + 120 - 12 - 10 + 6, 300, 20, new TranslationTextComponent("PLACEHOLDER")) {
        };

        this.children.add(this.inputEntityId);
        this.children.add(this.inputEntityStateToCheck);
        this.children.add(this.inputAttributeName);
        this.children.add(this.inputAttributeValueToCheck);

        this.children.add(this.outputEntityId);
        this.children.add(this.outputAttributeName);

        this.inputEntityId.setMaxLength(128);
        this.inputEntityStateToCheck.setMaxLength(64);
        this.inputAttributeName.setMaxLength(128);
        this.inputAttributeValueToCheck.setMaxLength(64);
        this.outputEntityId.setMaxLength(512);
        this.outputAttributeName.setMaxLength(512);

        this.outputEntityId.setBordered(false);
        this.outputAttributeName.setBordered(false);

        this.outputEntityId.setEditable(false);
        this.outputAttributeName.setEditable(false);

        this.inputEntityId.setValue(this.entityId);
        this.toggleEntityOperatorButton.setMessage(new TranslationTextComponent(this.entityOperator));
        this.inputEntityStateToCheck.setValue(this.entityStateToCheck);

        this.inputAttributeName.setValue(this.attributeName);
        this.toggleAttributeOperatorButton.setMessage(new TranslationTextComponent(this.attributeOperator));
        this.inputAttributeValueToCheck.setValue(this.attributeValueToCheck);

        this.outputEntityId.setValue(this.entityId + ": " + this.currentEntityState);
        this.outputAttributeName.setValue(this.attributeName.isEmpty() ? "" : this.attributeName + ": " + this.currentAttributeValue);

        if (this.entityId.isEmpty()) {
            this.outputEntityId.setVisible(false);
            this.outputAttributeName.setVisible(false);
        }

    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        drawCenteredString(matrixStack, this.font, "Set entity to listen for Block", this.width / 2, 20, 16777215);
        drawString(matrixStack, this.font, "Entity", this.width / 2 - 150, 40, 10526880);
        drawString(matrixStack, this.font, "Value", this.width / 2 + 15, 40, 10526880);
        drawString(matrixStack, this.font, "Attribute (optional)", this.width / 2 - 150, 80, 10526880);
        drawString(matrixStack, this.font, "Value", this.width / 2 + 15, 80, 10526880);

//        fill(matrixStack, this.width/ 2 - 150, this.height / 4 + 120 - 12 - 10 - 20, this.width/2 + 150, this.height / 4 + 120 - 12 - 10 + 20, -16777216);
        if (!this.entityId.isEmpty()) {
            fill(matrixStack, this.width / 2 - 150, this.height / 4 + 120 - 12 - 10 - 20, this.width / 2 + 150, this.height / 4 + 120 - 12 - 10 + 20, -16777216);
        }

        this.inputEntityId.render(matrixStack, mouseX, mouseY, partialTicks);
        this.inputEntityStateToCheck.render(matrixStack, mouseX, mouseY, partialTicks);
        this.inputAttributeName.render(matrixStack, mouseX, mouseY, partialTicks);
        this.inputAttributeValueToCheck.render(matrixStack, mouseX, mouseY, partialTicks);

        this.outputEntityId.render(matrixStack, mouseX, mouseY, partialTicks);
        this.outputAttributeName.render(matrixStack, mouseX, mouseY, partialTicks);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderBackground(MatrixStack p_230446_1_) {
        super.renderBackground(p_230446_1_);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean keyReleased(int p_223281_1_, int p_223281_2_, int p_223281_3_) {
        if (!isNumeric(this.inputEntityStateToCheck.getValue())) {
            if (!this.inputEntityStateToCheck.getValue().isEmpty()) {
                this.toggleEntityOperatorButton.setMessage(new TranslationTextComponent("="));
            }
        } else if (!isNumeric(this.inputAttributeValueToCheck.getValue())) {
            if (!this.inputAttributeValueToCheck.getValue().isEmpty()) {
                this.toggleAttributeOperatorButton.setMessage(new TranslationTextComponent("="));
            }
        }
        return super.keyReleased(p_223281_1_, p_223281_2_, p_223281_3_);
    }

    public void setOperatorIcon(TextFieldWidget toCheck, Button OperatorButton) {
        if (isNumeric(toCheck.getValue()) || toCheck.getValue().isEmpty()) {
            switch (OperatorButton.getMessage().getString()) {
                case "=":
                    OperatorButton.setMessage(new TranslationTextComponent(">"));
                    break;
                case ">":
                    OperatorButton.setMessage(new TranslationTextComponent("<"));
                    break;
                case "<":
                    OperatorButton.setMessage(new TranslationTextComponent("="));
                    break;
            }
        } else {
            OperatorButton.setMessage(new TranslationTextComponent("="));
        }
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    public void onDone() {
//        if (!this.inputEntityId.getValue().isEmpty()
//                && !this.inputEntityStateToCheck.getValue().isEmpty()
//                && (!this.inputEntityId.getValue().equals(entityId)
//                || !this.inputEntityStateToCheck.getValue().equals(entityStateToCheck)
//                || !this.toggleEntityOperatorButton.getMessage().getString().equals(entityOperator)
//                || !this.toggleAttributeOperatorButton.getMessage().getString().equals(attributeOperator))
//        ) {

        Network.CHANNEL.sendToServer(new UpdateStateBlockData(
                this.inputEntityId.getValue(),
                this.toggleEntityOperatorButton.getMessage().getString(),
                this.inputEntityStateToCheck.getValue(),
                this.inputAttributeName.getValue(),
                this.toggleAttributeOperatorButton.getMessage().getString(),
                this.inputAttributeValueToCheck.getValue(),
                this.blockPos));
//        }
        this.minecraft.setScreen(null);
    }
}
