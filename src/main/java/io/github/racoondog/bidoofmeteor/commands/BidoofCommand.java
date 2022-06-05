package io.github.racoondog.bidoofmeteor.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.racoondog.bidoofmeteor.ChatUtils;
import io.github.racoondog.bidoofmeteor.hud.BidoofImageHud;
import io.github.racoondog.bidoofmeteor.impl.PlayerHeadCacheImpl;
import meteordevelopment.meteorclient.systems.commands.Command;
import meteordevelopment.meteorclient.systems.hud.HUD;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

@Environment(EnvType.CLIENT)
public class BidoofCommand extends Command {
    public BidoofCommand() {
        super("bidoof", "bidoof");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("flushCache").executes(BidoofCommand::clearCache))
            .then(literal("hud")
                .then(literal("add").executes(BidoofCommand::addHudElement))
                .then(literal("remove").executes(BidoofCommand::removeHudElement)));
    }

    private static int clearCache(CommandContext<CommandSource> context) {
        PlayerHeadCacheImpl.DYNAMIC_PLAYER_HEAD_CACHE.clear();
        ChatUtils.info("Cleared Better Chat Head Cache.");
        return SINGLE_SUCCESS;
    }

    private static int addHudElement(CommandContext<CommandSource> context) {
        HUD.get().elements.add(new BidoofImageHud());
        ChatUtils.info("Added Hud Element.");
        return SINGLE_SUCCESS;
    }

    private static int removeHudElement(CommandContext<CommandSource> context) {
        if (BidoofImageHud.elements.size() > 1) {
            BidoofImageHud imageHud = BidoofImageHud.elements.get(BidoofImageHud.elements.size() - 1);
            HUD.get().elements.remove(imageHud);
            BidoofImageHud.elements.remove(imageHud);
            ChatUtils.info("Removed Hud Element.");
            return SINGLE_SUCCESS;
        } else {
            ChatUtils.error("Cannot have lower than 1 hud element.");
            return 0;
        }
    }
}
