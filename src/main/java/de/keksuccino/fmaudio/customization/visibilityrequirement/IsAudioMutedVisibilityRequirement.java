package de.keksuccino.fmaudio.customization.visibilityrequirement;

import de.keksuccino.fancymenu.api.visibilityrequirements.VisibilityRequirement;
import de.keksuccino.fmaudio.customization.item.ACIMuteHandler;
import de.keksuccino.konkrete.input.CharacterFilter;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class IsAudioMutedVisibilityRequirement extends VisibilityRequirement {

    public IsAudioMutedVisibilityRequirement() {
        super("fancymenu_extension:audio:visibilityrequirement:is_muted");
    }

    @Override
    public boolean hasValue() {
        return true;
    }

    @Override
    public boolean isRequirementMet(@Nullable String value) {

        if (value != null) {
            value = value.replace(" ", "");
            return ACIMuteHandler.isMuted(value);
        }

        return false;

    }

    @Override
    public String getDisplayName() {
        return Locals.localize("fancymenu.fmaudio.visibilityrequirement.is_muted");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(StringUtils.splitLines(Locals.localize("fancymenu.fmaudio.visibilityrequirement.is_muted.desc"), "%n%"));
    }

    @Override
    public String getValueDisplayName() {
        return Locals.localize("fancymenu.fmaudio.visibilityrequirement.is_muted.value");
    }

    @Override
    public String getValuePreset() {
        return "";
    }

    @Override
    public CharacterFilter getValueInputFieldFilter() {
        return null;
    }

}
