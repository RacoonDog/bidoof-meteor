package io.github.racoondog.bidoofmeteor.hud;

import io.github.racoondog.bidoofmeteor.mixininterface.IRenderer2D;
import meteordevelopment.meteorclient.renderer.GL;
import meteordevelopment.meteorclient.renderer.Renderer2D;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import static meteordevelopment.meteorclient.utils.Utils.WHITE;

public class ImageHud extends HudElement {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();
    public final Setting<String> image = this.sgGeneral.add(new StringSetting.Builder()
        .name("image")
        .description("The path of the image.")
        .defaultValue("minecraft:textures/misc/unknown_server.png")
        .onChanged(s -> this.updateTexture())
        .build()
    );
    private final Setting<Double> scale = this.sgGeneral.add(new DoubleSetting.Builder()
        .name("scale")
        .description("The scale of the image.")
        .defaultValue(2)
        .min(0.1)
        .sliderRange(0.1, 10)
        .build()
    );
    private final Setting<Boolean> hFlip = this.sgGeneral.add(new BoolSetting.Builder()
        .name("horizontal-flip")
        .description("Flips the image horizontally.")
        .defaultValue(false)
        .build()
    );
    private final Setting<Boolean> vFlip = this.sgGeneral.add(new BoolSetting.Builder()
        .name("vertical-flip")
        .description("Flips the image vertically.")
        .defaultValue(false)
        .build()
    );

    private Identifier TEXTURE;

    public ImageHud(HudElementInfo<?> info) {
        super(info);
        this.updateTexture();
    }

    public void updateTexture() {
        try {
            this.TEXTURE = new Identifier(this.image.get());
        } catch (InvalidIdentifierException ignored) {}
    }

    @Override
    public void updatePos() {
        super.updatePos();
        this.setSize(64 * this.scale.get(), 64 * this.scale.get());
    }

    @Override
    public void render(HudRenderer renderer) {
        GL.bindTexture(this.TEXTURE);
        Renderer2D.TEXTURE.begin();
        if (hFlip.get() && vFlip.get()) ((IRenderer2D)Renderer2D.TEXTURE).texQuadHVFlip(this.x, this.y, this.getWidth(), this.getHeight(), WHITE);
        else if (hFlip.get()) ((IRenderer2D)Renderer2D.TEXTURE).texQuadHFlip(this.x, this.y, this.getWidth(), this.getHeight(), WHITE);
        else if (vFlip.get()) ((IRenderer2D)Renderer2D.TEXTURE).texQuadVFlip(this.x, this.y, this.getWidth(), this.getHeight(), WHITE);
        else Renderer2D.TEXTURE.texQuad(this.x, this.y, this.getWidth(), this.getHeight(), WHITE);
        Renderer2D.TEXTURE.render(null);
    }
}
