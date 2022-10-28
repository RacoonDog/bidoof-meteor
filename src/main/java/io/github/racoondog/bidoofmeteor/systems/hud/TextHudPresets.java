package io.github.racoondog.bidoofmeteor.systems.hud;

import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.elements.TextHud;
import meteordevelopment.meteorclient.utils.PreInit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import static meteordevelopment.meteorclient.systems.hud.elements.MeteorTextHud.INFO;

@Environment(EnvType.CLIENT)
public class TextHudPresets {
    public static final HudElementInfo<TextHud>.Preset FAKE_POSITION = addPreset("Fake Position", "Pos: #1{floor(bidoof.fake_x)}, {floor(bidoof.fake_y)}, {floor(bidoof.fake_z)}", 0);
    public static final HudElementInfo<TextHud>.Preset MOD_COUNT = addPreset("Mod Count", "Running #1{mod} #0mods.");

    @PreInit
    public static void init() {}

    private static HudElementInfo<TextHud>.Preset addPreset(String title, String text) {
        return addPreset(title, text, -1);
    }

    private static HudElementInfo<TextHud>.Preset addPreset(String title, String text, int updateDelay) {
        return INFO.addPreset(title, (textHud) -> {
            if (text != null) {
                textHud.text.set(text);
            }

            if (updateDelay != -1) {
                textHud.updateDelay.set(updateDelay);
            }
        });
    }
}
