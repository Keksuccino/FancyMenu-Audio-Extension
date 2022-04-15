package de.keksuccino.fmaudio.customization.item;

import de.keksuccino.fancymenu.api.item.CustomizationItem;
import de.keksuccino.fancymenu.api.item.CustomizationItemContainer;
import de.keksuccino.fancymenu.api.item.LayoutEditorElement;
import de.keksuccino.fancymenu.menu.fancy.helper.layoutcreator.LayoutEditorScreen;
import de.keksuccino.fmaudio.customization.item.editor.AudioLayoutEditorElement;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class AudioCustomizationItemContainer extends CustomizationItemContainer {

    public AudioCustomizationItemContainer() {
        super("fancymenu_extension:audio_item");
    }

    @Override
    public CustomizationItem constructDefaultItemInstance() {
        PropertiesSection sec = new PropertiesSection("dummy");
        sec.addEntry("channel", "master");
        AudioCustomizationItem i = new AudioCustomizationItem(this, sec);
        i.width = 50;
        i.height = 50;
        return i;
    }

    @Override
    public CustomizationItem constructCustomizedItemInstance(PropertiesSection propertiesSection) {
        return new AudioCustomizationItem(this, propertiesSection);
    }

    @Override
    public LayoutEditorElement constructEditorElementInstance(CustomizationItem customizationItem, LayoutEditorScreen layoutEditorScreen) {
        return new AudioLayoutEditorElement(this, customizationItem, true, layoutEditorScreen);
    }

    @Override
    public String getDisplayName() {
        return Locals.localize("fancymenu.fmaudio.item");
    }

    @Override
    public String[] getDescription() {
        return StringUtils.splitLines(Locals.localize("fancymenu.fmaudio.item.desc"), "%n%");
    }

}
