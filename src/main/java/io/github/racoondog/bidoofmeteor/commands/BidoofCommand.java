package io.github.racoondog.bidoofmeteor.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.racoondog.bidoofmeteor.commands.arguments.PlayerArgumentType;
import io.github.racoondog.bidoofmeteor.impl.PlayerHeadCacheImpl;
import io.github.racoondog.bidoofmeteor.util.ChatUtils;
import meteordevelopment.meteorclient.systems.commands.Command;
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
            .then(literal("namehistory")
                .then(literal("username").then(argument("username", PlayerArgumentType.username()).executes(NameHistoryCommand::username)))
                .then(literal("uuid").then(argument("uuid", PlayerArgumentType.uuid()).executes(NameHistoryCommand::uuid)))
            );
    }

    private static int clearCache(CommandContext<CommandSource> context) {
        PlayerHeadCacheImpl.DYNAMIC_PLAYER_HEAD_CACHE.clear();
        ChatUtils.info("Cleared Better Chat Head Cache.");
        return SINGLE_SUCCESS;
    }
}
