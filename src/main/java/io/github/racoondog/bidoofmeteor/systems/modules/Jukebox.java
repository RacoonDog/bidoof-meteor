package io.github.racoondog.bidoofmeteor.systems.modules;

import com.google.common.collect.Lists;
import io.github.racoondog.bidoofmeteor.BidoofMeteor;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import meteordevelopment.meteorclient.gui.widgets.containers.WSection;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.containers.WVerticalList;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;

@Environment(EnvType.CLIENT)
public class Jukebox extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<Enum<Mode>> mode = sgGeneral.add(new EnumSetting.Builder<Enum<Mode>>()
        .name("mode")
        .defaultValue(Mode.Stop)
        .build()
    );

    private final Setting<List<String>> list = sgGeneral.add(new StringListSetting.Builder()
        .name("list")
        .defaultValue(Lists.newArrayList("minecraft:ambient.cave"))
        .visible(() -> !mode.get().equals(Mode.Stop))
        .build()
    );

    private final Setting<Boolean> repeat = sgGeneral.add(new BoolSetting.Builder()
        .name("repeat")
        .description("Makes music started via this module repeating.")
        .defaultValue(false)
        .build()
    );

    public Jukebox() {
        super(BidoofMeteor.CATEGORY, "jukebox", "Customize ingame sound.");
    }

    @Override
    public WWidget getWidget(GuiTheme theme) {
        WVerticalList list = theme.verticalList();

        WSection section = list.add(theme.section("Catalog", false)).expandX().widget();

        WTable table = section.add(theme.table()).expandX().widget();




        WButton button = table.add(theme.button("Stop Music")).expandX().widget();
        button.action = this::stop;
        WButton button2 = table.add(theme.button("Stop All")).expandX().widget();
        button2.action = () -> {
            mc.getMusicTracker().stop();
            mc.getSoundManager().stopAll();
        };
        table.row();

        int index = 0;

        for (var disc : Registry.ITEM.stream().filter(o -> o instanceof MusicDiscItem).map(MusicDiscItem.class::cast).toList()) {
            button(table, theme, Text.translatable(disc.getTranslationKey() + ".desc").getString().replace('Å', 'A'), disc.getSound()); //goofy ahh font issue
            index++;
            if (index % 2 == 0) table.row();
        }

        return list;
    }

    private void button(WContainer container, GuiTheme theme, String name, SoundEvent event) {
        WButton button = container.add(theme.button(name)).expandX().widget();
        button.action = () -> playSong(event);
    }

    private void stop() {
        mc.getMusicTracker().stop();
        mc.getSoundManager().stopSounds(null, SoundCategory.MUSIC);
        mc.getSoundManager().stopSounds(null, SoundCategory.RECORDS);
        mc.getSoundManager().stopSounds(null, SoundCategory.AMBIENT);
    }

    private void playSong(SoundEvent event) {
        stop();
        mc.getSoundManager().play(createInstance(event));
    }

    private PositionedSoundInstance createInstance(SoundEvent event) {
        return new PositionedSoundInstance(event.getId(), SoundCategory.RECORDS, 1.0f, 1.0f, SoundInstance.createRandom(), repeat.get(), 0, SoundInstance.AttenuationType.NONE, 0.0, 0.0, 0.0, true);
    }
    public static boolean shouldCancelMusic(Identifier identifier) {
        Jukebox jukebox = Modules.get().get(Jukebox.class);
        return jukebox.isActive() && (jukebox.mode.get().equals(Mode.Stop) || (jukebox.mode.get().equals(Mode.Blacklist) && jukebox.list.get().contains(identifier.toString())));
    }

    public enum Mode {
        Stop,
        Blacklist
    }
}
