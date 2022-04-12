package de.keksuccino.fmaudio;

import de.keksuccino.konkrete.config.ConfigEntry;
import de.keksuccino.konkrete.gui.screens.ConfigScreen;
import de.keksuccino.konkrete.localization.Locals;
import net.minecraft.client.gui.screens.Screen;

public class FmAudioConfigScreen extends ConfigScreen {

    public FmAudioConfigScreen(Screen parent) {
        super(FmAudio.config, Locals.localize("fancymenu.fmaudio.config"), parent);
    }

    @Override
    protected void init() {
        super.init();

        for (String s : this.config.getCategorys()) {
            this.setCategoryDisplayName(s, Locals.localize("fancymenu.fmaudio.config.categories." + s));
        }

        for (ConfigEntry e : this.config.getAllAsEntry()) {
            this.setValueDisplayName(e.getName(), Locals.localize("fancymenu.fmaudio.config." + e.getName()));
            this.setValueDescription(e.getName(), Locals.localize("fancymenu.fmaudio.config." + e.getName() + ".desc"));
        }

    }

}
