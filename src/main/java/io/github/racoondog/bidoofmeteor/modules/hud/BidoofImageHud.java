package io.github.racoondog.bidoofmeteor.modules.hud;

import com.google.common.collect.Lists;
import io.github.racoondog.bidoofmeteor.BidoofImages;
import io.github.racoondog.bidoofmeteor.mixininterface.IRenderer2D;
import meteordevelopment.meteorclient.renderer.GL;
import meteordevelopment.meteorclient.renderer.Renderer2D;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.hud.HUD;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.systems.hud.modules.HudElement;
import net.minecraft.util.Identifier;

import java.util.List;

import static meteordevelopment.meteorclient.utils.Utils.WHITE;

public class BidoofImageHud extends HudElement {
    public static final List<BidoofImageHud> elements = Lists.newArrayList();

    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<Enum<BidoofImages>> enumSetting = this.sgGeneral.add(new EnumSetting.Builder<Enum<BidoofImages>>()
        .name("type")
        .defaultValue(BidoofImages.Bidoof)
        .onChanged(anEnum -> updateTexture())
        .build()
    );

    private final Setting<String> image = this.sgGeneral.add(new StringSetting.Builder()
        .name("image")
        .description("The path of the image.")
        .defaultValue("minecraft:textures/misc/unknown_server.png")
        .visible(() -> enumSetting.get().equals(BidoofImages.Custom))
        .onChanged(s -> updateTexture())
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

    private Identifier TEXTURE = new Identifier(this.image.get());

    public BidoofImageHud() {
        super(HUD.get(), "bidoof-image-" + elements.size(), "Shows a Bidoof (tm) image in the HUD.");
        updateTexture();
        elements.add(this);
    }

    public void updateTexture() {
        if (this.enumSetting.get() instanceof BidoofImages bidoofEnum) {
            if (bidoofEnum.equals(BidoofImages.Custom)) this.TEXTURE = new Identifier(this.image.get());
            else this.TEXTURE = bidoofEnum.imagePath;
        }
    }

    @Override
    public void update(HudRenderer renderer) {
        this.box.setSize(64 * this.scale.get(), 64 * this.scale.get());
    }

    @Override
    public void render(HudRenderer renderer) {
        GL.bindTexture(this.TEXTURE);
        Renderer2D.TEXTURE.begin();
        if (hFlip.get() && vFlip.get()) ((IRenderer2D)Renderer2D.TEXTURE).texQuadHVFlip(this.box.getX(), this.box.getY(), this.box.width, this.box.height, WHITE);
        else if (hFlip.get()) ((IRenderer2D)Renderer2D.TEXTURE).texQuadHFlip(this.box.getX(), this.box.getY(), this.box.width, this.box.height, WHITE);
        else if (vFlip.get()) ((IRenderer2D)Renderer2D.TEXTURE).texQuadVFlip(this.box.getX(), this.box.getY(), this.box.width, this.box.height, WHITE);
        else Renderer2D.TEXTURE.texQuad(this.box.getX(), this.box.getY(), this.box.width, this.box.height, WHITE);
        Renderer2D.TEXTURE.render(null);
    }
}
