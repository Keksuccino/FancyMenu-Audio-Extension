package de.keksuccino.fmaudio.customization.item;

import de.keksuccino.auudio.audio.AudioClip;
import de.keksuccino.fancymenu.menu.button.ButtonCache;
import de.keksuccino.fancymenu.menu.button.ButtonCachedEvent;
import de.keksuccino.fancymenu.menu.fancy.MenuCustomization;
import de.keksuccino.fancymenu.menu.fancy.guicreator.CustomGuiBase;
import de.keksuccino.fancymenu.menu.fancy.helper.MenuReloadedEvent;
import de.keksuccino.fancymenu.menu.fancy.helper.layoutcreator.LayoutEditorScreen;
import de.keksuccino.fmaudio.FmAudio;
import de.keksuccino.fmaudio.audio.AudioHandler;
import de.keksuccino.fmaudio.events.PreScreenInitEvent;
import de.keksuccino.konkrete.Konkrete;
import de.keksuccino.konkrete.events.EventPriority;
import de.keksuccino.konkrete.events.SubscribeEvent;
import de.keksuccino.konkrete.events.client.ClientTickEvent;
import de.keksuccino.konkrete.events.client.GuiScreenEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ACIHandler {

    private static final Logger LOGGER = LogManager.getLogger("fancymenu/fmaudio/AudioCustomizationItemHandler");

    public static List<String> lastPlayingAudioSources = new ArrayList<>();
    public static List<String> newLastPlayingAudioSources = new ArrayList<>();

    public static Map<String, AudioCustomizationItem> currentNonLoopItems = new HashMap<>();
    public static Map<String, AudioCustomizationItem> startedOncePerSessionItems = new HashMap<>();

    protected static Screen lastScreen = null;

    public static boolean initialResourceReloadFinished = false;

    protected static Screen lastScreenCustom = null;
    public static boolean isNewCustomGui = false;
    protected static boolean newCustomGuiForTicker = false;

    public static void init() {
        Konkrete.getEventHandler().registerEventsFrom(new ACIHandler());
        ACIMuteHandler.init();
    }

    @SubscribeEvent
    public void onPrePreInit(PreScreenInitEvent e) {
        if (!ButtonCache.isCaching()) {
            if (lastScreenCustom != null) {
                if ((e.getScreen() instanceof CustomGuiBase) && (lastScreenCustom instanceof CustomGuiBase)) {
                    if (!((CustomGuiBase)e.getScreen()).getIdentifier().equals(((CustomGuiBase)lastScreenCustom).getIdentifier())) {
                        isNewCustomGui = true;
                    } else {
                        isNewCustomGui = false;
                    }
                } else {
                    isNewCustomGui = false;
                }
            } else {
                isNewCustomGui = false;
            }
            lastScreenCustom = e.getScreen();
            newCustomGuiForTicker = isNewCustomGui;
        }
    }

    @SubscribeEvent
    public void onReload(MenuReloadedEvent e) {
        currentNonLoopItems.clear();
        startedOncePerSessionItems.clear();
        AudioHandler.stopAll();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onButtonsCachedPre(ButtonCachedEvent e) {
        if (isNewMenu() && MenuCustomization.isValidScreen(e.getGui())) {
            currentNonLoopItems.clear();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onButtonsCachedPost(ButtonCachedEvent e) {
        if (isNewMenu() && MenuCustomization.isValidScreen(e.getGui())) {
            stopLastPlayingAudios();
        }
    }

    @SubscribeEvent
    public void onScreenInitPre(GuiScreenEvent.InitGuiEvent.Pre e) {
        if (e.getGui() instanceof LayoutEditorScreen) {
            AudioHandler.stopAll();
        }
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent.Pre e) {
        Screen current = Minecraft.getInstance().screen;
        if ((current == null) && (lastScreen != null)) {
            AudioHandler.stopAll();
        }
        lastScreen = current;
    }

    public static void stopLastPlayingAudios() {
        for (String s : lastPlayingAudioSources) {
            AudioClip c = AudioHandler.getAudioIfRegistered(s);
            if (c != null) {
                c.stop();
            }
        }
        lastPlayingAudioSources.clear();
        lastPlayingAudioSources.addAll(newLastPlayingAudioSources);
        newLastPlayingAudioSources.clear();
    }

    public static boolean isNewMenu() {
        if (MenuCustomization.isNewMenu() || isNewCustomGui) {
            return true;
        }
        return false;
    }

    public static boolean playingAllowed() {
        boolean onlyOutOfWorld = FmAudio.config.getOrDefault("only_play_out_of_world", false);
        if (onlyOutOfWorld && (Minecraft.getInstance().level != null)) {
            return false;
        }
        return true;
    }

}
