package io.github.racoondog.bidoofmeteor.util;

import meteordevelopment.meteorclient.addons.AddonManager;
import meteordevelopment.meteorclient.mixin.ChatHudAccessor;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
public class ChatUtils {
    private static Text PREFIX;

    public static void init() {
        Color color = AddonManager.METEOR.color.copy();
        ModMetadata meta = FabricLoader.getInstance().getModContainer("bidoof-meteor").get().getMetadata();
        if (meta.containsCustomValue("meteor-client:color")) color.parse(meta.getCustomValue("meteor-client:color").getAsString());

        PREFIX = new LiteralText("")
            .setStyle(Style.EMPTY.withFormatting(Formatting.GRAY))
            .append("[")
            .append(new LiteralText("Bidoof").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color.getPacked()))))
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
        BaseText message = new LiteralText(messageContent);
        message.setStyle(message.getStyle().withFormatting(messageColor));
        sendMsg(message);
    }

    public static void sendMsg(Text msg) {
        if (mc.world == null) return;

        BaseText message = new LiteralText("");
        message.append(PREFIX);
        message.append(msg);

        ((ChatHudAccessor) mc.inGameHud.getChatHud()).add(message, 0);
    }

    private static String formatMsg(String format, Formatting defaultColor, Object... args) {
        String msg = String.format(format, args);
        msg = msg.replaceAll("\\(default\\)", defaultColor.toString());
        msg = msg.replaceAll("\\(highlight\\)", Formatting.WHITE.toString());
        msg = msg.replaceAll("\\(underline\\)", Formatting.UNDERLINE.toString());

        return msg;
    }
}
