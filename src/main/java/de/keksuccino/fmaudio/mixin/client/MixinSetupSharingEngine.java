package de.keksuccino.fmaudio.mixin.client;

import de.keksuccino.fancymenu.menu.fancy.helper.SetupSharingEngine;
import de.keksuccino.fancymenu.menu.fancy.item.CustomizationItemBase;
import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.konkrete.properties.PropertiesSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.List;
import java.util.Map;

@Mixin(SetupSharingEngine.class)
public abstract class MixinSetupSharingEngine {

    @Inject(at = @At("RETURN"), method = "getLayoutResources", remap = false)
    private static void onGetLayoutResources(PropertiesSet layout, CallbackInfoReturnable<List<String>> info) {

        List<String> l = info.getReturnValue();
        if (l != null) {

            for (PropertiesSection s : layout.getPropertiesOfType("customization")) {
                String action = s.getEntryValue("action");
                if (action != null) {
                    if (action.equals("custom_layout_element:fancymenu_extension:audio_item")) {
                        for (Map.Entry<String, String> m : s.getEntries().entrySet()) {
                            if (m.getKey().startsWith("audio_source:")) {
                                String audioPath = null;
                                if (m.getValue().contains(";")) {
                                    audioPath = m.getValue().split("[;]")[0];
                                }
                                if (audioPath != null) {
                                    String shortPath = CustomizationItemBase.fixBackslashPath(SetupSharingEngine.getShortPath(CustomizationItemBase.fixBackslashPath(audioPath)));
                                    if (shortPath != null) {
                                        File f = new File(shortPath);
                                        if ((!audioPath.replace(" ", "").equals("")) && f.exists()) {
                                            if (!shortPath.endsWith("config/fancymenu") && !shortPath.endsWith("config/fancymenu/")) {
                                                l.add(shortPath);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

    }

}
