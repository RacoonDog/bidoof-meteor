package io.github.racoondog.bidoofmeteor.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.racoondog.bidoofmeteor.commands.arguments.PlayerArgumentType;
import io.github.racoondog.bidoofmeteor.util.ApiUtils;
import io.github.racoondog.bidoofmeteor.util.ChatUtils;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.command.CommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Environment(EnvType.CLIENT)
public class NameHistoryCommand {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public static int uuid(CommandContext<CommandSource> context) throws CommandSyntaxException {
        AtomicReference<CommandSyntaxException> ae = new AtomicReference<>();
        MeteorExecutor.execute(() -> {
            String uuid = PlayerArgumentType.getUUID(context, "uuid");
            try {
                nameHistory(uuid, uuid);
            } catch (CommandSyntaxException exception) {
                ae.set(exception);
            }
        });
        if (ae.get() != null) throw ae.get();

        return Command.SINGLE_SUCCESS;
    }

    public static int username(CommandContext<CommandSource> context) throws CommandSyntaxException {
        AtomicReference<CommandSyntaxException> ae = new AtomicReference<>();
        MeteorExecutor.execute(() -> {
            String username = PlayerArgumentType.getUsername(context, "username");
            String uuid = ApiUtils.uuidFromName(username);
            try {
                if (uuid == null) throw PlayerArgumentType.UsernameArgumentType.INVALID_USERNAME.create(username);
                nameHistory(uuid, username);
            } catch (CommandSyntaxException exception) {
                ae.set(exception);
            }
        });
        if (ae.get() != null) throw ae.get();

        return Command.SINGLE_SUCCESS;
    }

    public static void nameHistory(String uuid, String source) throws CommandSyntaxException {
        Map<String, Long> pastNames = ApiUtils.nameHistoryFromUuid(uuid);
        if (pastNames == null || pastNames.isEmpty()) throw PlayerArgumentType.UUIDArgumentType.INVALID_UUID.create(uuid);
        ChatUtils.empty();
        ChatUtils.info("(bold)%s's Name History:", source);
        ArrayList<Map.Entry<String, Long>> list = Lists.newArrayList(pastNames.entrySet()); //Map to list
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue())); //Reverse list order
        for (var entry : list) {
            Date dateTime = new Date(entry.getValue());
            String date = entry.getValue() == 0 ? "(highlight)Initial Username" : "Changed At (highlight)%s".formatted(DATE_FORMAT.format(dateTime));
            ChatUtils.info("(highlight)%s (default): %s", entry.getKey(), date);
            ChatUtils.sendMsg(Text.literal("View on NameMC").styled(style -> {
                style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://namemc.com/search?q=" + uuid));
                style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("View on NameMC").formatted(Formatting.YELLOW).formatted(Formatting.ITALIC)));
                return style;
            }));
        }
    }
}
