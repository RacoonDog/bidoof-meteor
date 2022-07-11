package io.github.racoondog.bidoofmeteor;

import io.github.racoondog.bidoofmeteor.commands.BidoofCommand;
import io.github.racoondog.bidoofmeteor.hud.ImageHudPresets;
import io.github.racoondog.bidoofmeteor.hud.TextHudPresets;
import io.github.racoondog.bidoofmeteor.modules.Announcer;
import io.github.racoondog.bidoofmeteor.modules.Logger;
import io.github.racoondog.bidoofmeteor.util.ChatUtils;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@Environment(EnvType.CLIENT)
public class BidoofMeteor extends MeteorAddon {
	public static final org.slf4j.Logger LOG = LoggerFactory.getLogger(BidoofMeteor.class);
	public static final Category CATEGORY = new Category("Bidoof");

	@Override
	public void onInitialize() {
		LOG.info("Initializing Bidoof Meteor");

        MeteorClient.EVENT_BUS.registerLambdaFactory("io.github.racoondog.bidoofmeteor", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));
        ChatUtils.init();

        Modules.get().add(new Logger());
        Modules.get().add(new Announcer());

        Commands.get().add(new BidoofCommand());

        Hud.get().register(ImageHudPresets.INFO);
        TextHudPresets.init();

        BidoofStarscript.init();
	}

	@Override
	public void onRegisterCategories() {
		Modules.registerCategory(CATEGORY);
	}

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("RacoonDog", "bidoof-meteor");
    }
}
