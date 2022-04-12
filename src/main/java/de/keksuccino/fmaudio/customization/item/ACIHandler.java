package de.keksuccino.fmaudio.customization.item;

import de.keksuccino.auudio.audio.AudioClip;
import de.keksuccino.fancymenu.events.SoftMenuReloadEvent;
import de.keksuccino.fancymenu.menu.button.ButtonCachedEvent;
import de.keksuccino.fancymenu.menu.fancy.MenuCustomization;
import de.keksuccino.fancymenu.menu.fancy.helper.MenuReloadedEvent;
import de.keksuccino.fancymenu.menu.fancy.helper.layoutcreator.LayoutEditorScreen;
import de.keksuccino.fmaudio.audio.AudioHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new ACIHandler());
        ACIMuteHandler.init();
    }

    @SubscribeEvent
    public void onReload(MenuReloadedEvent e) {
        currentNonLoopItems.clear();
        startedOncePerSessionItems.clear();
        AudioHandler.stopAll();
    }

    @SubscribeEvent
    public void onSoftReload(SoftMenuReloadEvent e) {
        currentNonLoopItems.clear();
        startedOncePerSessionItems.clear();
        AudioHandler.stopAll();
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onButtonsCachedPre(ButtonCachedEvent e) {
        if (MenuCustomization.isNewMenu() && MenuCustomization.isValidScreen(e.getGui())) {
            currentNonLoopItems.clear();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onButtonsCachedPost(ButtonCachedEvent e) {
        if (MenuCustomization.isNewMenu() && MenuCustomization.isValidScreen(e.getGui())) {
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
    public void onTick(TickEvent.ClientTickEvent e) {
        Screen current = Minecraft.getInstance().currentScreen;
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

}
