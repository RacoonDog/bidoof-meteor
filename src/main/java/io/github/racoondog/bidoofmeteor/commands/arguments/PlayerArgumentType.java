package io.github.racoondog.bidoofmeteor.commands.arguments;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.racoondog.bidoofmeteor.mixininterface.IStringReader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
public class PlayerArgumentType {
    public static UsernameArgumentType username() {
        return new UsernameArgumentType();
    }

    public static String getUsername(final CommandContext<CommandSource> context, final String argumentName) {
        return context.getArgument(argumentName, String.class);
    }

    public static UUIDArgumentType uuid() {
        return new UUIDArgumentType();
    }

    public static String getUUID(final CommandContext<CommandSource> context, final String argumentName) {
        return context.getArgument(argumentName, String.class);
    }

    public static class UsernameArgumentType implements ArgumentType<String> {
        private static final Collection<String> EXAMPLES = List.of("seasnail8169", "MineGame159");
        private static final Pattern USERNAME_REGEX = Pattern.compile("\\w{1,16}$");
        public static final DynamicCommandExceptionType INVALID_USERNAME = new DynamicCommandExceptionType(o -> new LiteralMessage(o + " is not a valid username."));

        @Override
        public String parse(StringReader reader) throws CommandSyntaxException {
            String string = ((IStringReader)reader).readUsername();

            if (string.length() < 1 || string.length() > 16 || !USERNAME_REGEX.matcher(string).matches()) throw INVALID_USERNAME.create(string);

            return string;
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return CommandSource.suggestMatching(mc.world.getPlayers().stream().map(PlayerEntity::getEntityName), builder);
        }

        @Override
        public Collection<String> getExamples() {
            return EXAMPLES;
        }
    }

    public static class UUIDArgumentType implements ArgumentType<String> {
        private static final Collection<String> EXAMPLES = List.of("3aa4fb57-2ef7-4304-b4d7-bd2187f48e1f", "c6fb9580fcbb42608d2a2b7704a8daaf");
        private static final Pattern UUID_REGEX = Pattern.compile("[-a-fA-F\\d]{32,36}$");
        public static final DynamicCommandExceptionType INVALID_UUID = new DynamicCommandExceptionType(o -> new LiteralMessage(o + " is not a valid UUID."));

        @Override
        public String parse(StringReader reader) throws CommandSyntaxException {
            String string = ((IStringReader)reader).readUUID();

            if (string.length() < 32 || string.length() > 36 || !UUID_REGEX.matcher(string).matches()) throw INVALID_UUID.create(string);

            return string;
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return CommandSource.suggestMatching(mc.world.getPlayers().stream().map(PlayerEntity::getUuidAsString), builder);
        }

        @Override
        public Collection<String> getExamples() {
            return EXAMPLES;
        }
    }
}
