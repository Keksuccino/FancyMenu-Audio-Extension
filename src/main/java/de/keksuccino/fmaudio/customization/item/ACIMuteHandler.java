package de.keksuccino.fmaudio.customization.item;

import de.keksuccino.fancymenu.menu.fancy.MenuCustomizationProperties;
import de.keksuccino.fmaudio.FmAudio;
import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.konkrete.properties.PropertiesSerializer;
import de.keksuccino.konkrete.properties.PropertiesSet;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ACIMuteHandler {

    private static final File MUTED_ITEMS_FILE = new File(FmAudio.INSTANCE_DATA_DIR.getPath() + "/muted_audio_items.properties");

    protected static List<String> mutedItems = new ArrayList<>();

    public static void init() {
        readProperties();
        removeUnusedItems();
    }

    public static void setMuted(String id, boolean muted) {

        if (muted) {
            if (!mutedItems.contains(id)) {
                mutedItems.add(id);
            }
        } else {
            if (mutedItems.contains(id)) {
                mutedItems.remove(id);
            }
        }

        writeProperties();

    }

    public static boolean isMuted(String id) {
        return mutedItems.contains(id);
    }

    protected static void removeUnusedItems() {

        try {

            List<PropertiesSet> sets = new ArrayList<>();
            sets.addAll(MenuCustomizationProperties.getProperties());
            sets.addAll(MenuCustomizationProperties.getDisabledProperties());

            List<String> audioItemIdentifiers = new ArrayList<>();

            for (PropertiesSet set : sets) {
                for (PropertiesSection sec : set.getPropertiesOfType("customization")) {
                    String action = sec.getEntryValue("action");
                    if ((action != null) && action.equals("custom_layout_element:fancymenu_extension:audio_item")) {
                        String id = sec.getEntryValue("actionid");
                        if (id != null) {
                            audioItemIdentifiers.add(id);
                        }
                    }
                }
            }

            List<String> removeItems = new ArrayList<>();
            for (String s : mutedItems) {
                if (!audioItemIdentifiers.contains(s)) {
                    removeItems.add(s);
                }
            }
            for (String s : removeItems) {
                mutedItems.remove(s);
            }

            writeProperties();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected static void writeProperties() {

        try {

            if (!MUTED_ITEMS_FILE.isFile()) {
                MUTED_ITEMS_FILE.createNewFile();
            }

            PropertiesSet set = new PropertiesSet("muted_audio_items");
            PropertiesSection sec = new PropertiesSection("muted-items");

            for (String s : mutedItems) {
                sec.addEntry(s, "---");
            }

            set.addProperties(sec);

            PropertiesSerializer.writeProperties(set, MUTED_ITEMS_FILE.getPath());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected static void readProperties() {

        try {

            mutedItems.clear();

            if (!MUTED_ITEMS_FILE.isFile()) {
                writeProperties();
            }

            PropertiesSet set = PropertiesSerializer.getProperties(MUTED_ITEMS_FILE.getPath());
            if (set != null) {
                List<PropertiesSection> secs = set.getPropertiesOfType("muted-items");
                if (!secs.isEmpty()) {
                    PropertiesSection sec = secs.get(0);
                    for (Map.Entry<String, String> m : sec.getEntries().entrySet()) {
                        mutedItems.add(m.getKey());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
