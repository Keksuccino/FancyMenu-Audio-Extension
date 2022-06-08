//TODO übernehmen
package de.keksuccino.fmaudio.mixin.client;

import de.keksuccino.fmaudio.events.PreScreenInitEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Screen.class)
public class MixinScreen {

    @Inject(at = @At(value = "HEAD"), method = "init(Lnet/minecraft/client/Minecraft;II)V")
    private void onInit(Minecraft minecraft, int width, int height, CallbackInfo info) {
        PreScreenInitEvent e = new PreScreenInitEvent((Screen)((Object)this));
        MinecraftForge.EVENT_BUS.post(e);
    }

}
