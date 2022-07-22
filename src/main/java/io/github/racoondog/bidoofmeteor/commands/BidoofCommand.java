package io.github.racoondog.bidoofmeteor.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.racoondog.bidoofmeteor.commands.arguments.PlayerArgumentType;
import io.github.racoondog.bidoofmeteor.commands.arguments.Vec3ArgumentType;
import io.github.racoondog.bidoofmeteor.impl.PlayerHeadCacheImpl;
import io.github.racoondog.bidoofmeteor.util.ApiUtils;
import io.github.racoondog.bidoofmeteor.util.ChatUtils;
import meteordevelopment.meteorclient.systems.commands.Command;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.util.math.Vec3d;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

@Environment(EnvType.CLIENT)
public class BidoofCommand extends Command {
    public BidoofCommand() {
        super("bidoof", "bidoof");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("flushCache").executes(BidoofCommand::clearCache))
            .then(literal("getuuid").then(argument("username", PlayerArgumentType.username()).executes(BidoofCommand::getUuid)))
            .then(literal("namehistory")
                .then(literal("username").then(argument("username", PlayerArgumentType.username()).executes(NameHistoryCommand::username)))
                .then(literal("uuid").then(argument("uuid", PlayerArgumentType.uuid()).executes(NameHistoryCommand::uuid))))
            .then(literal("lookat").then(argument("location", Vec3ArgumentType.vec3()).executes(BidoofCommand::lookAt)));
    }

    private static int lookAt(CommandContext<CommandSource> context) {
        Vec3d target = Vec3ArgumentType.getCVec3(context, "location");
        assert mc.player != null;
        mc.player.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, target);
        ChatUtils.info("Looking at (highlight)%s(default), (highlight)%s(default), (highlight)%s(default).", target.x, target.y, target.z);
        return SINGLE_SUCCESS;
    }

    private static int clearCache(CommandContext<CommandSource> context) {
        PlayerHeadCacheImpl.DYNAMIC_PLAYER_HEAD_CACHE.clear();
        ChatUtils.info("Cleared Better Chat head cache.");
        return SINGLE_SUCCESS;
    }

    //todo replace warning with CommandSyntaxException
    private static int getUuid(CommandContext<CommandSource> context) {
        ApiUtils.API_EXECUTOR_SERVICE.execute(() -> {
            String username = PlayerArgumentType.getUsername(context, "username");
            String uuid = ApiUtils.uuidFromName(username);
            if (uuid == null) {
                ChatUtils.warning("(highlight)%s(default) did not return a UUID. (highlight)Invalid name?(default)", username);
            } else {
                ChatUtils.info("(highlight)%s(default)'s UUID is (highlight)%s(default).", username, uuid);
            }
        });
        return SINGLE_SUCCESS;
    }
}
