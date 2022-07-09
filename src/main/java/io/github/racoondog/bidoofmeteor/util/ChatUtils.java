package io.github.racoondog.bidoofmeteor.util;

import meteordevelopment.meteorclient.addons.AddonManager;
import meteordevelopment.meteorclient.mixin.ChatHudAccessor;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
public class ChatUtils {
    private static Text PREFIX;

    public static void init() {
        Color color = AddonManager.METEOR.color.copy();
        ModMetadata meta = FabricLoader.getInstance().getModContainer("bidoof-meteor").get().getMetadata();
        if (meta.containsCustomValue("meteor-client:color")) color.parse(meta.getCustomValue("meteor-client:color").getAsString());

        PREFIX = Text.literal("")
            .setStyle(Style.EMPTY.withFormatting(Formatting.GRAY))
            .append("[")
            .append(Text.literal("Bidoof").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color.getPacked()))))
            .append("] ");
    }

    // Default
    public static void info(String message, Object... args) {
        sendMsg(Formatting.GRAY, message, args);
    }

    public static void info(String message) {
        sendMsg(message, Formatting.GRAY);
    }

    // Warning
    public static void warning(String message, Object... args) {
        sendMsg(Formatting.YELLOW, message, args);
    }

    public static void warning(String message) {
        sendMsg(message, Formatting.YELLOW);
    }

    // Error
    public static void error(String message, Object... args) {
        sendMsg(Formatting.RED, message, args);
    }

    public static void error(String message) {
        sendMsg(message, Formatting.RED);
    }

    // Misc
    public static void sendMsg(Formatting messageColor, String messageContent, Object... args) {
        sendMsg(formatMsg(messageContent, messageColor, args), messageColor);
    }

    public static void sendMsg(String messageContent, Formatting messageColor) {
        MutableText message = Text.literal(messageContent);
        message.setStyle(message.getStyle().withFormatting(messageColor));
        sendMsg(message);
    }

    public static void sendMsg(Text msg) {
        if (mc.world == null) return;

        MutableText message = Text.literal("");
        message.append(PREFIX);
        message.append(msg);

        ((ChatHudAccessor) mc.inGameHud.getChatHud()).add(message, 0);
    }

    private static String formatMsg(String format, Formatting defaultColor, Object... args) {
        String msg = String.format(format, args);
        msg = msg.replace("(default)", defaultColor.toString());
        msg = msg.replace("(highlight)", Formatting.WHITE.toString());
        msg = msg.replace("(bold)", Formatting.BOLD.toString());
        msg = msg.replace("(underline)", Formatting.UNDERLINE.toString());

        return msg;
    }

    public static void empty() {
        if (mc.world == null) return;
        ((ChatHudAccessor) mc.inGameHud.getChatHud()).add(Text.empty(), 0);
    }
}
