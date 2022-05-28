package de.keksuccino.fmaudio.events;

import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.eventbus.api.Event;

public class PreScreenInitEvent extends Event {

    Screen screen;

    public PreScreenInitEvent(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }

}
