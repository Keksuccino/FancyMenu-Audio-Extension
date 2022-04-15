package de.keksuccino.fmaudio;

import com.mojang.blaze3d.vertex.PoseStack;
import de.keksuccino.fancymenu.menu.fancy.helper.ui.FMConfigScreen;
import de.keksuccino.fancymenu.menu.fancy.helper.ui.UIBase;
import de.keksuccino.konkrete.events.SubscribeEvent;
import de.keksuccino.konkrete.events.client.GuiScreenEvent;
import de.keksuccino.konkrete.gui.content.AdvancedImageButton;
import de.keksuccino.konkrete.localization.Locals;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class EventHandler {

    protected static final ResourceLocation SETTINGS_ICON_LOCATION = new ResourceLocation("fmaudio", "textures/settings_button.png");

    protected AdvancedImageButton openSettingsButton;

    public EventHandler() {

        this.openSettingsButton = new AdvancedImageButton(-10, 80 + 35 + 5, 44, 35, SETTINGS_ICON_LOCATION, true, (press) -> {
            Minecraft.getInstance().setScreen(new FmAudioConfigScreen(Minecraft.getInstance().screen));
        }) {
            @Override
            public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
                this.setDescription(Locals.localize("fancymenu.fmaudio.config"));
                if (this.isHovered()) {
                    this.setX(-2);
                } else {
                    this.setX(-10);
                }
                super.render(matrixStack, mouseX, mouseY, partialTicks);
            }
        };
        UIBase.colorizeButton(this.openSettingsButton);

    }

    @SubscribeEvent
    public void onDrawScreenPost(GuiScreenEvent.DrawScreenEvent.Post e) {

        if (e.getGui() instanceof FMConfigScreen) {

            this.openSettingsButton.render(e.getMatrixStack(), e.getMouseX(), e.getMouseY(), e.getRenderPartialTicks());

        }

    }

}
