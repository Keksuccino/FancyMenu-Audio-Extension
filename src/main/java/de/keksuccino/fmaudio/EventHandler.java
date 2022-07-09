package de.keksuccino.fmaudio;

import com.mojang.blaze3d.vertex.PoseStack;
import de.keksuccino.fancymenu.menu.fancy.helper.ui.FMConfigScreen;
import de.keksuccino.fancymenu.menu.fancy.helper.ui.UIBase;
import de.keksuccino.konkrete.gui.content.AdvancedImageButton;
import de.keksuccino.konkrete.localization.Locals;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
                if (this.isHoveredOrFocused()) {
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
    public void onDrawScreenPost(ScreenEvent.Render.Post e) {

        if (e.getScreen() instanceof FMConfigScreen) {

            this.openSettingsButton.render(e.getPoseStack(), e.getMouseX(), e.getMouseY(), e.getPartialTick());

        }

    }

}
