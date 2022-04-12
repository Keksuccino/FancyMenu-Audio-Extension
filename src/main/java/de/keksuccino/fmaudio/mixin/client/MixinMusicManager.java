package de.keksuccino.fmaudio.mixin.client;

import de.keksuccino.fmaudio.FmAudio;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MusicTicker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(MusicTicker.class)
public class MixinMusicManager {

    @Shadow @Nullable private ISound currentMusic;
    private boolean paused = false;

    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    private void onTick(CallbackInfo info) {

        if (Minecraft.getInstance().world != null) {
            if ((Minecraft.getInstance().currentScreen != null) && Minecraft.getInstance().currentScreen.isPauseScreen()) {
                if (FmAudio.config.getOrDefault("stop_world_music_in_menu", false)) {
                    if ((this.currentMusic != null) && !paused) {
                        Minecraft.getInstance().getSoundHandler().pause();
                        this.paused = true;
                    }
                    info.cancel();
                }
            } else {
                if (this.paused) {
                    Minecraft.getInstance().getSoundHandler().resume();
                    this.paused = false;
                }
            }
        }

    }

}
