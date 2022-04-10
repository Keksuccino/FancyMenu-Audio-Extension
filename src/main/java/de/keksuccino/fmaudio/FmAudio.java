package de.keksuccino.fmaudio;

import java.io.File;

import de.keksuccino.fancymenu.api.item.CustomizationItemRegistry;
import de.keksuccino.fmaudio.customization.item.AudioCustomizationItemContainer;
import de.keksuccino.fmaudio.customization.item.AudioCustomizationItemHandler;
import de.keksuccino.konkrete.localization.Locals;
import net.minecraft.resources.ResourceLocation;
import de.keksuccino.konkrete.Konkrete;
import de.keksuccino.konkrete.config.Config;
import de.keksuccino.konkrete.config.exceptions.InvalidValueException;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TODO "toggle mute audio element" button action adden
// - setzt "mute" boolean in audio element anhand der action ID auf true/false
// - mute state von elementen wird in file gespeichert (anhand von action ID)

//TODO change icon of audio element in editor to music note (same icon as used for Auudio)

@Mod("fmextension_audio")
public class FmAudio {

	public static final String VERSION = "1.0.0";

	private static final Logger LOGGER = LogManager.getLogger("fmaudio/FmAudio");

	public static final File MOD_DIR = new File("config/fancymenu/extensions/fmaudio");

	public static Config config;

	public FmAudio() {
		try {

			//Check if mod was loaded client- or server-side
			if (FMLEnvironment.dist == Dist.CLIENT) {

				if (!MOD_DIR.exists()) {
					MOD_DIR.mkdirs();
				}

				updateConfig();

				AudioCustomizationItemHandler.init();

				//Register audio item container
				CustomizationItemRegistry.registerItem(new AudioCustomizationItemContainer());

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

			config.registerValue("dummy_value", false, "general");

			config.syncConfig();

			config.clearUnusedValues();

		} catch (InvalidValueException e) {
			e.printStackTrace();
		}
	}

}
