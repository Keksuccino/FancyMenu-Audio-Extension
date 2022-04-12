package de.keksuccino.fmaudio.customization.item.editor;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.keksuccino.fancymenu.api.item.CustomizationItem;
import de.keksuccino.fancymenu.api.item.CustomizationItemContainer;
import de.keksuccino.fancymenu.api.item.LayoutEditorElement;
import de.keksuccino.fancymenu.menu.fancy.helper.layoutcreator.LayoutEditorScreen;
import de.keksuccino.fmaudio.customization.item.AudioCustomizationItem;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;

public class AudioLayoutEditorElement extends LayoutEditorElement {

    public AudioLayoutEditorElement(CustomizationItemContainer parentContainer, CustomizationItem customizationItemInstance, boolean destroyable, LayoutEditorScreen handler) {
        super(parentContainer, customizationItemInstance, destroyable, handler);
    }

    @Override
    public void init() {

        this.delayable = false;

        super.init();

        AudioCustomizationItem i = (AudioCustomizationItem) this.object;

        AdvancedButton manageAudiosButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.fmaudio.audio.manage"), (press) -> {
            ManageAudiosScreen s = new ManageAudiosScreen(this.handler, this);
            Minecraft.getInstance().displayGuiScreen(s);
            this.rightclickMenu.closeMenu();
        });
        manageAudiosButton.setDescription(StringUtils.splitLines(Locals.localize("fancymenu.fmaudio.audio.manage.desc"), "%n%"));
        this.rightclickMenu.addContent(manageAudiosButton);

        AdvancedButton channelButton = new AdvancedButton(0, 0, 0, 0, "", (press) -> {
            if (i.channel == SoundCategory.MASTER) {
                i.channel = SoundCategory.MUSIC;
            } else if (i.channel == SoundCategory.MUSIC) {
                i.channel = SoundCategory.RECORDS;
            } else if (i.channel == SoundCategory.RECORDS) {
                i.channel = SoundCategory.WEATHER;
            } else if (i.channel == SoundCategory.WEATHER) {
                i.channel = SoundCategory.BLOCKS;
            } else if (i.channel == SoundCategory.BLOCKS) {
                i.channel = SoundCategory.HOSTILE;
            } else if (i.channel == SoundCategory.HOSTILE) {
                i.channel = SoundCategory.NEUTRAL;
            } else if (i.channel == SoundCategory.NEUTRAL) {
                i.channel = SoundCategory.PLAYERS;
            } else if (i.channel == SoundCategory.PLAYERS) {
                i.channel = SoundCategory.AMBIENT;
            } else if (i.channel == SoundCategory.AMBIENT) {
                i.channel = SoundCategory.VOICE;
            } else if (i.channel == SoundCategory.VOICE) {
                i.channel = SoundCategory.MASTER;
            }
        }) {
            @Override
            public void render(MatrixStack p_93657_, int p_93658_, int p_93659_, float p_93660_) {
                this.setMessage(Locals.localize("fancymenu.fmaudio.audio.channel", i.channel.getName().toUpperCase()));
                super.render(p_93657_, p_93658_, p_93659_, p_93660_);
            }
        };
        this.rightclickMenu.addContent(channelButton);

        AdvancedButton loopButton = new AdvancedButton(0, 0, 0, 0, "", (press) -> {
            if (i.loop) {
                i.loop = false;
            } else {
                i.loop = true;
            }
        }) {
            @Override
            public void render(MatrixStack p_93657_, int p_93658_, int p_93659_, float p_93660_) {
                if (i.oncePerSession) {
                    this.active = false;
                } else {
                    this.active = true;
                }
                if (i.loop) {
                    this.setMessage(Locals.localize("fancymenu.fmaudio.audio.loop.on"));
                } else {
                    this.setMessage(Locals.localize("fancymenu.fmaudio.audio.loop.off"));
                }
                super.render(p_93657_, p_93658_, p_93659_, p_93660_);
            }
        };
        this.rightclickMenu.addContent(loopButton);

        AdvancedButton oncePerSessionButton = new AdvancedButton(0, 0, 0, 0, "", (press) -> {
            if (i.oncePerSession) {
                i.oncePerSession = false;
            } else {
                i.oncePerSession = true;
            }
        }) {
            @Override
            public void render(MatrixStack p_93657_, int p_93658_, int p_93659_, float p_93660_) {
                if (i.oncePerSession) {
                    this.setMessage(Locals.localize("fancymenu.fmaudio.audio.once_per_session.on"));
                } else {
                    this.setMessage(Locals.localize("fancymenu.fmaudio.audio.once_per_session.off"));
                }
                super.render(p_93657_, p_93658_, p_93659_, p_93660_);
            }
        };
        oncePerSessionButton.setDescription(StringUtils.splitLines(Locals.localize("fancymenu.fmaudio.audio.once_per_session.desc"), "%n%"));
        this.rightclickMenu.addContent(oncePerSessionButton);

    }

    @Override
    public SimplePropertiesSection serializeItem() {

        AudioCustomizationItem i = (AudioCustomizationItem) this.object;

        SimplePropertiesSection sec = new SimplePropertiesSection();
        sec.addEntry("channel", i.channel.getName());
        sec.addEntry("loop", "" + i.loop);
        sec.addEntry("once_per_session", "" + i.oncePerSession);
        for (AudioCustomizationItem.MenuAudio m : i.audios) {
            if ((m.path != null) && (m.soundType != null)) {
                sec.addEntry("audio_source:" + m.audioIdentifier, m.path + ";" + m.soundType.name() + ";" + m.volume + ";" + m.index);
            }
        }

        return sec;

    }

}
