package de.keksuccino.fmaudio.customization.item;

import de.keksuccino.auudio.audio.AudioClip;
import de.keksuccino.fancymenu.events.GuiInitCompletedEvent;
import de.keksuccino.fancymenu.events.SoftMenuReloadEvent;
import de.keksuccino.fancymenu.menu.button.ButtonCache;
import de.keksuccino.fancymenu.menu.button.ButtonCachedEvent;
import de.keksuccino.fancymenu.menu.fancy.MenuCustomization;
import de.keksuccino.fancymenu.menu.fancy.guicreator.CustomGuiBase;
import de.keksuccino.fancymenu.menu.fancy.helper.MenuReloadedEvent;
import de.keksuccino.fancymenu.menu.fancy.helper.layoutcreator.LayoutEditorScreen;
import de.keksuccino.fmaudio.FmAudio;
import de.keksuccino.fmaudio.audio.AudioHandler;
import de.keksuccino.fmaudio.events.PreScreenInitEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ACIHandler {

    private static final Logger LOGGER = LogManager.getLogger("fmaudio/ACIHandler");

    //TODO übernehmen
    public static volatile List<String> currentLayoutAudios = new ArrayList<>();
    //TODO übernehmen (volatile)
    public static volatile List<String> lastPlayingAudioSources = new ArrayList<>();
    public static volatile List<String> newLastPlayingAudioSources = new ArrayList<>();
    //-------------------

    //TODO übernehmen
//    public static Map<String, AudioCustomizationItem> currentNonLoopItems = new HashMap<>();
    //TODO übernehmen
//    public static Map<String, AudioCustomizationItem> startedOncePerSessionItems = new HashMap<>();

    protected static Screen lastScreen = null;

    protected static Screen lastScreenCustom = null;
    public static boolean isNewCustomGui = false;
    protected static boolean newCustomGuiForTicker = false;

    //TODO übernehmen
    public static Screen lastScreenGlobal = null;

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new ACIHandler());
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
            //TODO übernehmen
            currentLayoutAudios.clear();
        }
    }

    @SubscribeEvent
    public void onReload(MenuReloadedEvent e) {
        //TODO übernehmen
        AudioCustomizationItem.cachedItems.clear();
        AudioHandler.stopAll();
    }

    @SubscribeEvent
    public void onSoftReload(SoftMenuReloadEvent e) {
        //TODO übernehmen
        AudioCustomizationItem.cachedItems.clear();
        AudioHandler.stopAll();
    }

    //TODO übernehmen
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onButtonsCachedPre(ButtonCachedEvent e) {
        if (isNewMenu() && MenuCustomization.isValidScreen(e.getScreen())) {
            for (AudioCustomizationItem i : AudioCustomizationItem.cachedItems.values()) {
                if (i.isLoadingNextAudio) {
                    i.tryKillNextAudioThread = true;
                }
            }
        }
    }

    //TODO übernehmen
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onButtonsCachedPost(ButtonCachedEvent e) {
        if (MenuCustomization.isValidScreen(e.getScreen())) {
            stopLastPlayingAudios();
        }
    }

    //TODO übernehmen
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onInitCompleted(GuiInitCompletedEvent e) {
        if (!ButtonCache.isCaching() && MenuCustomization.isValidScreen(e.getScreen())) {
            lastScreenGlobal = Minecraft.getInstance().screen;
        }
    }

    @SubscribeEvent
    public void onScreenInitPre(ScreenEvent.Init.Pre e) {
        if (e.getScreen() instanceof LayoutEditorScreen) {
            AudioHandler.stopAll();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
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
