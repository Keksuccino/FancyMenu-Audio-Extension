package de.keksuccino.fmaudio;

import java.io.File;

import de.keksuccino.fancymenu.api.buttonaction.ButtonActionRegistry;
import de.keksuccino.fancymenu.api.item.CustomizationItemRegistry;
import de.keksuccino.fancymenu.api.visibilityrequirements.VisibilityRequirementRegistry;
import de.keksuccino.fmaudio.customization.buttonaction.ToggleMuteButtonAction;
import de.keksuccino.fmaudio.customization.item.AudioCustomizationItemContainer;
import de.keksuccino.fmaudio.customization.item.ACIHandler;
import de.keksuccino.fmaudio.customization.visibilityrequirement.IsAudioMutedVisibilityRequirement;
import de.keksuccino.konkrete.localization.Locals;
import net.minecraft.resources.ResourceLocation;
import de.keksuccino.konkrete.Konkrete;
import de.keksuccino.konkrete.config.Config;
import de.keksuccino.konkrete.config.exceptions.InvalidValueException;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("fmextension_audio")
public class FmAudio {

	//TODO übernehmen
	public static final String VERSION = "1.1.1";

	private static final Logger LOGGER = LogManager.getLogger("fmaudio/FmAudio");

	public static final File MOD_DIR = new File("config/fancymenu/extensions/fmaudio");
	public static final File INSTANCE_DATA_DIR = new File("fancymenu_instance_data/extensions/fmaudio");

	public static Config config;

	public FmAudio() {
		try {

			//Check if mod was loaded client- or server-side
			if (FMLEnvironment.dist == Dist.CLIENT) {

				if (!MOD_DIR.isDirectory()) {
					MOD_DIR.mkdirs();
				}

				if (!INSTANCE_DATA_DIR.isDirectory()) {
					INSTANCE_DATA_DIR.mkdirs();
				}

				updateConfig();

				ACIHandler.init();

				//Register audio item container
				CustomizationItemRegistry.registerItem(new AudioCustomizationItemContainer());
				//Register "Toggle Mute" button action
				ButtonActionRegistry.registerButtonAction(new ToggleMuteButtonAction());
				//Register "Is Muted" visibility requirement
				VisibilityRequirementRegistry.registerRequirement(new IsAudioMutedVisibilityRequirement());

				MinecraftForge.EVENT_BUS.register(new EventHandler());

				Konkrete.addPostLoadingEvent("fmextension_audio", this::onClientSetup);

			} else {
				LOGGER.warn("WARNING: FancyMenu Audio Extension is a client mod and has no effect when loaded on a server!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void onClientSetup() {
		try {

			initLocals();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void initLocals() {
		String baseDir = "locals/";
		File f = new File(MOD_DIR.getPath() + "/locals");
		if (!f.exists()) {
			f.mkdirs();
		}

		Locals.copyLocalsFileToDir(new ResourceLocation("fmaudio", baseDir + "en_us.local"), "en_us", f.getPath());
//        Locals.copyLocalsFileToDir(new ResourceLocation("fmaudio", baseDir + "de_de.local"), "de_de", f.getPath());

		Locals.getLocalsFromDir(f.getPath());
	}

	public static void updateConfig() {
		try {

			config = new Config(MOD_DIR.getPath() + "/config.cfg");

			config.registerValue("stop_world_music_in_menu", false, "world");
			config.registerValue("only_play_out_of_world", false, "world");

			config.syncConfig();

			config.clearUnusedValues();

		} catch (InvalidValueException e) {
			e.printStackTrace();
		}
	}

}
