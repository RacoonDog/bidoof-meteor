package io.github.racoondog.bidoofmeteor.hud;

import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ImageHudPresets {
    public static final HudElementInfo<ImageHud> INFO = new HudElementInfo<>(Hud.GROUP, "image", "Displays arbitrary images.", ImageHudPresets::create);
    public static final HudElementInfo<ImageHud>.Preset BIDOOF = addPreset("Bidoof", "bidoof-meteor:textures/bidoof.png");
    public static final HudElementInfo<ImageHud>.Preset BIDOOF_METEOR = addPreset("Bidoof Meteor", "bidoof-meteor:textures/bidoofmeteor.png");
    public static final HudElementInfo<ImageHud>.Preset METEOR = addPreset("Meteor", "meteor-client:textures/meteor.png");

    private static HudElementInfo<ImageHud>.Preset addPreset(String title, String identifier) {
        return INFO.addPreset(title, imageHud -> {
            if (identifier != null) imageHud.image.set(identifier);
        });
    }

    private static ImageHud create() {
        return new ImageHud(INFO);
    }
}
