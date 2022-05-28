package de.keksuccino.fmaudio.events;

import de.keksuccino.konkrete.events.EventBase;
import net.minecraft.client.gui.screens.Screen;

public class PreScreenInitEvent extends EventBase {

    Screen screen;

    public PreScreenInitEvent(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

}
