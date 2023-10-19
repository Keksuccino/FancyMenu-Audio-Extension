package de.keksuccino.fmaudio;

import de.keksuccino.fancymenu.events.RenderScreenEvent;
import net.minecraft.client.gui.GuiGraphics;
import de.keksuccino.fancymenu.menu.fancy.helper.ui.FMConfigScreen;
import de.keksuccino.fancymenu.menu.fancy.helper.ui.UIBase;
import de.keksuccino.konkrete.events.SubscribeEvent;
import de.keksuccino.konkrete.gui.content.AdvancedImageButton;
import de.keksuccino.konkrete.localization.Locals;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("all")
public class EventHandler {

    protected static final ResourceLocation SETTINGS_ICON_LOCATION = new ResourceLocation("fmaudio", "textures/settings_button.png");

    protected AdvancedImageButton openSettingsButton;

    public EventHandler() {

        this.openSettingsButton = new AdvancedImageButton(-10, 80 + 35 + 5, 44, 35, SETTINGS_ICON_LOCATION, true, (press) -> {
            Minecraft.getInstance().setScreen(new FmAudioConfigScreen(Minecraft.getInstance().screen));
        }) {
            @Override
            public void render(GuiGraphics graphicsStack, int mouseX, int mouseY, float partialTicks) {
                this.setDescription(Locals.localize("fancymenu.fmaudio.config"));
                if (this.isHoveredOrFocused()) {
                    this.setX(-2);
                } else {
                    this.setX(-10);
                }
                super.render(graphicsStack, mouseX, mouseY, partialTicks);
            }
        };
        UIBase.colorizeButton(this.openSettingsButton);

    }

    @SubscribeEvent
    public void onDrawScreenPost(RenderScreenEvent.Post e) {

        if (e.getScreen() instanceof FMConfigScreen) {

            this.openSettingsButton.render(e.getGuiGraphics(), e.getMouseX(), e.getMouseY(), e.getRenderPartialTicks());

        }

    }

}
