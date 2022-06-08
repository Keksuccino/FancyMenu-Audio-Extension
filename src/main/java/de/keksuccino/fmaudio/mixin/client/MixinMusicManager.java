package de.keksuccino.fmaudio.mixin.client;

import de.keksuccino.fmaudio.FmAudio;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicManager.class)
public class MixinMusicManager {

    @Shadow private SoundInstance currentMusic;

    private boolean paused = false;

    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    private void onTick(CallbackInfo info) {

        if (Minecraft.getInstance().level != null) {
            if ((Minecraft.getInstance().screen != null) && Minecraft.getInstance().screen.isPauseScreen()) {
                if (FmAudio.config.getOrDefault("stop_world_music_in_menu", false)) {
                    if ((this.currentMusic != null) && !paused) {
                        Minecraft.getInstance().getSoundManager().pause();
                        this.paused = true;
                    }
                    info.cancel();
                }
            } else {
                if (this.paused) {
                    Minecraft.getInstance().getSoundManager().resume();
                    this.paused = false;
                }
            }
        }

    }

}
