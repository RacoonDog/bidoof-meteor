package io.github.racoondog.bidoofmeteor.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.racoondog.bidoofmeteor.commands.arguments.PlayerArgumentType;
import io.github.racoondog.bidoofmeteor.util.ApiUtils;
import io.github.racoondog.bidoofmeteor.util.ChatUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.command.CommandSource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class NameHistoryCommand {
    public static int uuid(CommandContext<CommandSource> context) throws CommandSyntaxException {
        String uuid = PlayerArgumentType.getUUID(context, "uuid");
        nameHistory(uuid, uuid);
        return 1;
    }

    public static int username(CommandContext<CommandSource> context) throws CommandSyntaxException {
        String username = PlayerArgumentType.getUsername(context, "username");
        String uuid = ApiUtils.uuidFromName(username);
        if (uuid == null) throw PlayerArgumentType.UsernameArgumentType.INVALID_USERNAME.create(username);
        nameHistory(uuid, username);
        return 1;
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
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String date = entry.getValue() == 0 ? "(highlight)Initial Username" : "Changed At (highlight)%s".formatted(dateFormat.format(dateTime));
            ChatUtils.info("(highlight)%s (default): %s", entry.getKey(), date);
        }
    }
}
