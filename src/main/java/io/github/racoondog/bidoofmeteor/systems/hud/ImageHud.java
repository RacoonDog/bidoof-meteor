package io.github.racoondog.bidoofmeteor.systems.hud;

import io.github.racoondog.bidoofmeteor.mixininterface.IRenderer2D;
import meteordevelopment.meteorclient.renderer.GL;
import meteordevelopment.meteorclient.renderer.Renderer2D;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.utils.network.Http;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.io.IOException;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
public class ImageHud extends HudElement {
    public static final HudElementInfo<ImageHud> INFO = new HudElementInfo<>(Hud.GROUP, "image", "Displays arbitrary images.", ImageHud::create);

    private static ImageHud create() {
        return new ImageHud(INFO);
    }

    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();
    private final SettingGroup sgCustomization = this.settings.createGroup("Customization");

    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .defaultValue(Mode.Presets)
        .onChanged(s -> this.updateTexture())
        .build()
    );

    private final Setting<Preset> preset = sgGeneral.add(new EnumSetting.Builder<Preset>()
        .name("preset")
        .defaultValue(Preset.Bidoof)
        .visible(() -> mode.get().equals(Mode.Presets))
        .onChanged(s -> this.updateTexture())
        .build()
    );

    public final Setting<String> image = sgGeneral.add(new StringSetting.Builder()
        .name("image")
        .description("The path of the image.")
        .defaultValue("minecraft:textures/misc/unknown_server.png")
        .visible(() -> mode.get().equals(Mode.Resource))
        .onChanged(s -> this.updateTexture())
        .build()
    );

    public final Setting<String> url = sgGeneral.add(new StringSetting.Builder()
        .name("url")
        .description("The url of the image.")
        .defaultValue("https://raw.githubusercontent.com/RacoonDog/RacoonDog/main/csnail.PNG")
        .visible(() -> mode.get().equals(Mode.Online))
        .onChanged(s -> this.updateTexture())
        .build()
    );

    private final Setting<Double> scale = sgCustomization.add(new DoubleSetting.Builder()
        .name("scale")
        .description("The scale of the image.")
        .defaultValue(2)
        .min(0.1)
        .sliderRange(0.1, 10)
        .onChanged(o -> updateSize())
        .build()
    );
    private final Setting<Boolean> hFlip = sgCustomization.add(new BoolSetting.Builder()
        .name("horizontal-flip")
        .description("Flips the image horizontally.")
        .defaultValue(false)
        .build()
    );
    private final Setting<Boolean> vFlip = sgCustomization.add(new BoolSetting.Builder()
        .name("vertical-flip")
        .description("Flips the image vertically.")
        .defaultValue(false)
        .build()
    );

    private final Setting<SettingColor> colorFilter = sgCustomization.add(new ColorSetting.Builder()
        .name("color-filter")
        .defaultValue(new SettingColor(255, 255, 255))
        .build()
    );

    private static final Identifier ONLINE = new Identifier("bidoof-meteor", "online");
    private Identifier TEXTURE;
    private int width;
    private int height;

    public ImageHud(HudElementInfo<?> info) {
        super(info);
        this.updateTexture();
        this.updateSize();
    }

    public void updateTexture() {
        switch (mode.get()) {
            case Presets -> this.TEXTURE = preset.get().identifier;
            case Resource -> this.TEXTURE = Identifier.tryParse(this.image.get());
            case Online -> {
                this.TEXTURE = ONLINE;
                MeteorExecutor.execute(() -> {
                    String imgUrl = url.get();
                    if (imgUrl.isEmpty()) return;
                    try {
                        NativeImage img = NativeImage.read(Http.get(imgUrl).sendInputStream());
                        this.width = img.getWidth();
                        this.height = img.getHeight();
                        mc.getTextureManager().registerTexture(ONLINE, new NativeImageBackedTexture(img));
                        this.updateSize();
                    } catch (IOException ignored) {}
                });
            }
        }
    }

    public void updateSize() {
        if (mode.get().equals(Mode.Online)) this.setSize(this.width * scale.get(), this.height * scale.get());
        else this.setSize(64 * scale.get(), 64 * scale.get());
    }

    @Override
    public void render(HudRenderer renderer) {
        GL.bindTexture(this.TEXTURE);
        Renderer2D.TEXTURE.begin();
        if (hFlip.get() && vFlip.get()) ((IRenderer2D)Renderer2D.TEXTURE).texQuadHVFlip(this.x, this.y, this.getWidth(), this.getHeight(), colorFilter.get());
        else if (hFlip.get()) ((IRenderer2D)Renderer2D.TEXTURE).texQuadHFlip(this.x, this.y, this.getWidth(), this.getHeight(), colorFilter.get());
        else if (vFlip.get()) ((IRenderer2D)Renderer2D.TEXTURE).texQuadVFlip(this.x, this.y, this.getWidth(), this.getHeight(), colorFilter.get());
        else Renderer2D.TEXTURE.texQuad(this.x, this.y, this.getWidth(), this.getHeight(), colorFilter.get());
        Renderer2D.TEXTURE.render(null);
    }

    public enum Mode {
        Presets,
        Resource,
        Online
    }

    public enum Preset {
        Bidoof("bidoof-meteor:textures/bidoof.png"),
        Bidoof_Meteor("bidoof-meteor:textures/bidoofmeteor.png"),
        Meteor("meteor-client:textures/meteor.png");

        final Identifier identifier;

        Preset(String location) {
            this.identifier = Identifier.tryParse(location);
        }
    }
}
