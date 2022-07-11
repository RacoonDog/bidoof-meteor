package io.github.racoondog.bidoofmeteor.hud;

import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.elements.TextHud;

import static meteordevelopment.meteorclient.systems.hud.elements.MeteorTextHud.INFO;
public class TextHudPresets {
    public static final HudElementInfo<TextHud>.Preset FAKE_POSITION;

    public static void init() {}

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

    static {
        FAKE_POSITION = addPreset("Fake Position", "Pos: #1{floor(bidoof.fake_x)}, {floor(bidoof.fake_y)}, {floor(bidoof.fake_z)}", 0);
    }
}
