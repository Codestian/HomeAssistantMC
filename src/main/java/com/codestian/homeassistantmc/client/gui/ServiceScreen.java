package com.codestian.homeassistantmc.client.gui;

import com.codestian.homeassistantmc.network.Network;
import com.codestian.homeassistantmc.network.message.UpdateServiceBlockData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedHashMap;

@OnlyIn(Dist.CLIENT)
public class ServiceScreen extends Screen {

    private final String entityConfig;
    private final BlockPos blockPos;
    private final Yaml yaml = new Yaml();
    private final Gson gson = new Gson();
    protected Button clearButton;
    protected Button copyButton;
    protected Button pasteButton;
    protected Button doneButton;
    protected Button cancelButton;
    protected TextFieldWidget inputServiceConfig;

    public ServiceScreen(ITextComponent p_i51108_1_, String entityConfig, BlockPos blockPos) {
        super(p_i51108_1_);
        this.entityConfig = entityConfig;
        this.blockPos = blockPos;
    }

    @Override
    protected void init() {
        super.init();
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);

        this.clearButton = this.addButton(new Button(this.width / 2 - 50 - 100 - 4, 80, 100, 20, new TranslationTextComponent("Clear"), (p_214187_1_) -> {
            this.onClear();
        }));
        this.copyButton = this.addButton(new Button(this.width / 2 - 50, 80, 100, 20, new TranslationTextComponent("Copy"), (p_214186_1_) -> {
            this.onCopy();
        }));
        this.pasteButton = this.addButton(new Button(this.width / 2 + 50 + 4, 80, 100, 20, new TranslationTextComponent("Paste"), (p_214186_1_) -> {
            this.onPaste();
        }));

        this.doneButton = this.addButton(new Button(this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20, DialogTexts.GUI_DONE, (p_214187_1_) -> {
            this.onDone();
        }));
        this.cancelButton = this.addButton(new Button(this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20, DialogTexts.GUI_CANCEL, (p_214186_1_) -> {
            this.onClose();
        }));

        this.inputServiceConfig = new TextFieldWidget(this.font, this.width / 2 - 150, 50, 300, 20, new TranslationTextComponent("advMode.command")) {
        };

        this.inputServiceConfig.setMaxLength(1024);
        this.inputServiceConfig.setEditable(false);

        this.children.add(this.inputServiceConfig);

        this.inputServiceConfig.setValue(entityConfig != null ? entityConfig : "");

    }

    public void onClear() {
        this.inputServiceConfig.setValue("");
    }

    public void onCopy() {
        if (
                !this.inputServiceConfig.getValue().equals("YAML key service is improper") ||
                        !this.inputServiceConfig.getValue().equals("AML configuration requires service and data keys") ||
                        !this.inputServiceConfig.getValue().equals("Please input valid YAML configuration")
        ) {
            Minecraft.getInstance().keyboardHandler.setClipboard(this.inputServiceConfig.getValue());
        }
    }

    public void onPaste() {

        String text = Minecraft.getInstance().keyboardHandler.getClipboard();
        try {
            Object yamlText = yaml.load(text);

            JsonObject jsonObject = gson.fromJson(gson.toJson(yamlText, LinkedHashMap.class), JsonObject.class);

            if (jsonObject.has("service")) {
                if (jsonObject.get("service").getAsString().chars().filter(num -> num == '.').count() == 1) {
                    this.inputServiceConfig.setValue(jsonObject.toString());
                } else {
                    this.inputServiceConfig.setValue("YAML key service is improper");
                }
            } else {
                this.inputServiceConfig.setValue("YAML configuration requires service and data keys");
            }
        } catch (Exception e) {
            this.inputServiceConfig.setValue("Please input valid YAML configuration");
        }
    }

    public void onDone() {
        Network.CHANNEL.sendToServer(new UpdateServiceBlockData(this.inputServiceConfig.getValue(), this.blockPos));
        this.minecraft.setScreen(null);
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        drawCenteredString(matrixStack, this.font, "Set call service config for Block", this.width / 2, 20, 16777215);
        drawString(matrixStack, this.font, "Set yaml config", this.width / 2 - 150, 40, 10526880);
        this.inputServiceConfig.render(matrixStack, mouseX, mouseY, partialTicks);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderBackground(MatrixStack p_230446_1_) {
        super.renderBackground(p_230446_1_);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}