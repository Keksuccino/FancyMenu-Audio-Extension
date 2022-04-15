package de.keksuccino.fmaudio.customization.buttonaction;

import de.keksuccino.fancymenu.api.buttonaction.ButtonActionContainer;
import de.keksuccino.fmaudio.customization.item.ACIMuteHandler;
import de.keksuccino.konkrete.localization.Locals;

public class ToggleMuteButtonAction extends ButtonActionContainer {

    public ToggleMuteButtonAction() {
        super("fancymenu_extension:audio:buttonaction:toggle_mute");
    }

    @Override
    public String getAction() {
        return "toggle_mute_audio";
    }

    @Override
    public boolean hasValue() {
        return true;
    }

    @Override
    public void execute(String value) {

        if (value != null) {
            value = value.replace(" ", "");
            if (ACIMuteHandler.isMuted(value)) {
                ACIMuteHandler.setMuted(value, false);
            } else {
                ACIMuteHandler.setMuted(value, true);
            }
        }

    }

    @Override
    public String getActionDescription() {
        return Locals.localize("fancymenu.fmaudio.buttonaction.togglemute.action.desc");
    }

    @Override
    public String getValueDescription() {
        return Locals.localize("fancymenu.fmaudio.buttonaction.togglemute.value.desc");
    }

    @Override
    public String getValueExample() {
        return "ddff1ceb-1502-4cd3-b1e4-9a0e0fb97a071649544796272";
    }

}
