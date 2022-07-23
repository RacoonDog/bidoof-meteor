package io.github.racoondog.bidoofmeteor;

import com.mojang.logging.LogUtils;
import io.github.racoondog.bidoofmeteor.commands.BidoofCommand;
import io.github.racoondog.bidoofmeteor.hud.ImageHudPresets;
import io.github.racoondog.bidoofmeteor.hud.TextHudPresets;
import io.github.racoondog.bidoofmeteor.modules.*;
import io.github.racoondog.bidoofmeteor.util.ChatUtils;
import meteordevelopment.meteorclient.MeteorClient;
import io.github.racoondog.bidoofmeteor.themes.DarkPurpleTheme;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.systems.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.lang.invoke.MethodHandles;

@Environment(EnvType.CLIENT)
public class BidoofMeteor extends MeteorAddon {
    public static final org.slf4j.Logger LOG = LogUtils.getLogger();
	public static final Category CATEGORY = new Category("Bidoof");
    public static BidoofMeteor INSTANCE;

	@Override
	public void onInitialize() {
		LOG.info("Initializing Bidoof Meteor");
        INSTANCE = this;

        Modules.get().add( new Logger() );
        Modules.get().add( new Announcer() );
        Modules.get().add( new FishyDetector() );
        Modules.get().add( new SpamPlus() );
        Modules.get().add( new CommandSubstituter() );
        Modules.get().add( new ChatCommands() );
        Modules.get().add( new AutoTpa() );
        Modules.get().add( new ChatEmotes() );

        Commands.get().add( new BidoofCommand() );

        Hud.get().register(ImageHudPresets.INFO);
        TextHudPresets.init();

        BidoofStarscript.init();

        //add(new Logger Module - Saves important information on certain events such as death in the logs.());
        GuiThemes.add( new DarkPurpleTheme() );

        //add(new Announcer Module - Re-added the removed Announcer module from Meteor Client.());
        //add(new SpamPlus Module - Spam the bee movie script.());
        //add(new Command Substituter Module - Automatically replaces commands sent with different commands.());
        //add(new Chat Commands Module - Turn your account into a Discord bot.());
        //add(new Auto TPA Module - Automatically accept TPA requests based on a whitelist.());
        //add(new Chat Emotes Module - Discord emotes for Minecraft, vanilla compatible.());
        //add(new Image HUD Element - Customizable image HUD Element.());
        //add(new Fake Position Text HUD Element - Fake your position in your HUD.());
        //add(new getUuid Command - Get the UUID of any account based on their username.());
        //add(new nameHistory Command - Get the name history of any account based on either username or UUID.());
        //add(new lookAt Command - Look at any location in the world.());
        //add(new Player Head Cache - Keep player heads in chat even after the player has left. Part of BetterChat());
        //add(new Anvil Tooltip Info - Anvil-related info, xp cost & anvil uses, of items in the tooltip. Part of BetterTooltips());
        //add(new Disables DiscordPresence by default.());
        //add(new Dark Purple Theme.());
	}

	@Override
	public void onRegisterCategories() {
		Modules.registerCategory(CATEGORY);
	}

    @Override
    public String getPackage() {
        return "io.github.racoondog.bidoofmeteor";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("RacoonDog", "bidoof-meteor");
    }
}
