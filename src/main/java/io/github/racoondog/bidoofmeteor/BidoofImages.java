package io.github.racoondog.bidoofmeteor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public enum BidoofImages { //Bidoof images do not work in dev env, but they do when compiled
    Bidoof(id("bidoof")),
    BidoofMeteor(id("bidoofmeteor")),
    Meteor(new Identifier("meteor-client", "textures/meteor.png")),
    Custom(null);

    public final Identifier imagePath;

    BidoofImages(Identifier imagePath) {
        this.imagePath = imagePath;
    }

    private static Identifier id(String path) {
        return new Identifier("bidoof-meteor", "textures/" + path + ".png");
    }
}
