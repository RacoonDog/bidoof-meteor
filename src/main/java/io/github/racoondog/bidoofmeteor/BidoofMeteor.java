package io.github.racoondog.bidoofmeteor;

import com.mojang.logging.LogUtils;
import io.github.racoondog.bidoofmeteor.commands.BidoofCommand;
import io.github.racoondog.bidoofmeteor.hud.ImageHud;
import io.github.racoondog.bidoofmeteor.modules.*;
import io.github.racoondog.bidoofmeteor.themes.DarkPurpleTheme;
import io.github.racoondog.bidoofmeteor.util.AddonUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.systems.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.misc.DiscordPresence;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Items;

@Environment(EnvType.CLIENT)
public class BidoofMeteor extends MeteorAddon {
    public static final org.slf4j.Logger LOG = LogUtils.getLogger();
	public static final Category CATEGORY = new Category("Bidoof", Items.AMETHYST_SHARD.getDefaultStack());
    public static BidoofMeteor INSTANCE;

	@Override
	public void onInitialize() {
		LOG.info("Initializing Bidoof Meteor");
        INSTANCE = this;

        if (!AddonUtils.areAddonsPresent("Meteor Rejects")) Modules.get().add( new Logger() );
        Modules.get().add( new Announcer() );
        Modules.get().add( new FishyDetector() );
        Modules.get().add( new SpamPlus() );
        Modules.get().add( new ChatCommands() );
        Modules.get().add( new AutoTpa() );
        Modules.get().add( new ChatEmotes() );
        Modules.get().add( new PingSpoofer() );
        Modules.get().add( new Jukebox() );
        Modules.get().add( new NoFallPlace() );

        Commands.get().add( new BidoofCommand() );

        Hud.get().register( ImageHud.INFO );

        GuiThemes.add( new DarkPurpleTheme() );

        DiscordPresence.registerCustomState("com.wildfire.gui.screen", "Changing options");

        //add(new PingSpoofer Module - Artificially modify your ping.());
        //add(new Jukebox Module - Control vanilla ingame music.());
        //add(new Logger Module - Saves important information on certain events such as death in the logs, configurable via Starscript.());
        //add(new Announcer Module - Re-added the removed Announcer module from Meteor Client.());
        //add(new SpamPlus Module - Spam the bee movie script.());
        //add(new Chat Commands Module - Turn your account into a Discord bot.());
        //add(new Auto TPA Module - Automatically accept TPA requests based on a whitelist.());
        //add(new Chat Emotes Module - Discord emotes for Minecraft, vanilla compatible.());
        //add(new Image HUD Element - Customizable image HUD Element.());
        //add(new Fake Position Text HUD Element - Fake your position in your HUD.());
        //add(new getUuid Command - Get the UUID of any account based on their username.());
        //add(new nameHistory Command - Get the name history of any account both online and offline based on either username or UUID.());
        //add(new lookAt Command - Automatically rotate to look at any location in the world.());
        //add(new coordinates Command - Copy your coordinates to your clipboard.());
        //add(new Player Head Cache - Keep player heads in chat even after the player has left. Part of BetterChat());
        //add(new Anvil Tooltip Info - Anvil-related info, xp cost & anvil uses, of items in the tooltip. Part of BetterTooltips());
        //add(new Stops DiscordPresence from being enabled by default.());
        //add(new Adds multiple math functions to Starscript.());
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
