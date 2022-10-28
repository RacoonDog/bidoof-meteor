package io.github.racoondog.bidoofmeteor.systems.modules;

import io.github.racoondog.bidoofmeteor.BidoofMeteor;
import meteordevelopment.meteorclient.events.game.SendMessageEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ChatEmotes extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final SettingGroup sgEmoteGroups = this.settings.createGroup("Emote Groups");

    public final Setting<Boolean> variations = sgGeneral.add(new BoolSetting.Builder()
        .name("variations")
        .description("Enables multiple variations of the same chat emote.")
        .defaultValue(true)
        .onChanged(e -> recomputeEmojiMap())
        .build()
    );

    public final Setting<Boolean> minecraft = sgEmoteGroups.add(new BoolSetting.Builder()
        .name("minecraft")
        .defaultValue(true)
        .onChanged(e -> recomputeEmojiMap())
        .build()
    );

    public final Setting<Boolean> alphabet = sgEmoteGroups.add(new BoolSetting.Builder()
        .name("alphabet")
        .defaultValue(true)
        .onChanged(e -> recomputeEmojiMap())
        .build()
    );

    public final Setting<Boolean> music = sgEmoteGroups.add(new BoolSetting.Builder()
        .name("music")
        .defaultValue(true)
        .onChanged(e -> recomputeEmojiMap())
        .build()
    );

    public final Setting<Boolean> symbols = sgEmoteGroups.add(new BoolSetting.Builder()
        .name("symbols")
        .defaultValue(true)
        .onChanged(e -> recomputeEmojiMap())
        .build()
    );

    public final Setting<Boolean> math = sgEmoteGroups.add(new BoolSetting.Builder()
        .name("math")
        .defaultValue(true)
        .onChanged(e -> recomputeEmojiMap())
        .build()
    );

    public final Setting<Boolean> emoticons = sgEmoteGroups.add(new BoolSetting.Builder()
        .name("emoticons")
        .defaultValue(true)
        .onChanged(e -> recomputeEmojiMap())
        .build()
    );

    public final Setting<Boolean> games = sgEmoteGroups.add(new BoolSetting.Builder()
        .name("games")
        .defaultValue(true)
        .onChanged(e -> recomputeEmojiMap())
        .build()
    );

    public final Setting<Boolean> miscellanious = sgEmoteGroups.add(new BoolSetting.Builder()
        .name("miscellanious")
        .defaultValue(true)
        .onChanged(e -> recomputeEmojiMap())
        .build()
    );

    public ChatEmotes() {
        super(BidoofMeteor.CATEGORY, "chat-emotes", "Enables the usage of emotes in the chat.");
        recomputeEmojiMap();
    }

    @EventHandler
    private void onMessageSend(SendMessageEvent event) {
        event.message = emoteReplacer(event.message);
    }

    private static final List<Pair<String, String>> EMOJI_MAP = new ArrayList<>();

    private static void ps(String v, String... ks) {
        for (var k : ks) {
            p(k, v);
        }
    }

    private static void p(String k, String v) {
        k = new StringBuilder().append(":").append(k).append(":").toString();
        EMOJI_MAP.add(new Pair<>(k, v));
    }

    private static String emoteReplacer(String msg) {
        for (var entry : EMOJI_MAP) {
            if (msg.contains(entry.getLeft())) msg = msg.replace(entry.getLeft(), entry.getRight());
        }
        return msg;
    }

    private void recomputeEmojiMap() {
        EMOJI_MAP.clear();
        boolean variations = this.variations.get();
        if (this.minecraft.get()) {
            p("sword", "🗡");
            p("shield", "🛡");
            p("axe", "🪓");
            p("bow", "🏹");
            p("trident", "🔱");
            p("rod", "\uD83C\uDFA3\"");
            p("potion", "🧪");
            p("fire", "🔥");
            p("shears", "✂");
            p("pick", "⛏");
            p("lightning", "⚡");
            p("bell", "🔔");
            p("crossed_swords", "⚔");
            p("meteor", "☄");
            p("cloud", "☁");
            p("meat", "🍖");
            p("snowman", "☃");
            ps("🌧", "cloud", "raincloud");
        }
        if (this.alphabet.get()) {
            p("1", "➀");
            p("2", "➁");
            p("3", "➂");
            p("4", "➃");
            p("5", "➄");
            p("6", "➅");
            p("7", "➆");
            p("8", "➇");
            p("9", "➈");
            p("10", "➉");
            p("A", "Ⓐ");
            p("B", "Ⓑ");
            p("C", "Ⓒ");
            p("D", "Ⓓ");
            p("E", "Ⓔ");
            p("F", "Ⓕ");
            p("G", "Ⓖ");
            p("H", "Ⓗ");
            p("I", "Ⓘ");
            p("J", "Ⓙ");
            p("K", "Ⓚ");
            p("L", "Ⓛ");
            p("M", "Ⓜ");
            p("N", "Ⓝ");
            p("O", "Ⓞ");
            p("P", "Ⓟ");
            p("Q", "Ⓠ");
            p("R", "Ⓡ");
            p("S", "Ⓢ");
            p("T", "Ⓣ");
            p("U", "Ⓤ");
            p("V", "Ⓥ");
            p("W", "Ⓦ");
            p("X", "Ⓧ");
            p("Y", "Ⓨ");
            p("Z", "Ⓩ");
            p("a", "ⓐ");
            p("b", "ⓑ");
            p("c", "ⓒ");
            p("d", "ⓓ");
            p("e", "ⓔ");
            p("f", "ⓕ");
            p("g", "ⓖ");
            p("h", "ⓗ");
            p("i", "ⓘ");
            p("j", "ⓙ");
            p("k", "ⓚ");
            p("l", "ⓛ");
            p("m", "ⓜ");
            p("n", "ⓝ");
            p("o", "ⓞ");
            p("p", "ⓟ");
            p("q", "ⓠ");
            p("r", "ⓡ");
            p("s", "ⓢ");
            p("t", "ⓣ");
            p("u", "ⓤ");
            p("v", "ⓥ");
            p("w", "ⓦ");
            p("x", "ⓧ");
            p("y", "ⓨ");
            p("z", "ⓩ");
        }
        if (this.music.get()) {
            p("eighth", "♪");
            p("quarter", "♩");
            p("beamed_eighth", "♫");
            p("beamed_sixteenth", "♬");
            p("flat", "♭");
        }
        if (this.symbols.get()) {
            p("scorpius", "♏");
            p("aquarius", "♒");
            p("aries", "♈");
            p("mercury", "☿");
            p("wheelchair", "♿");
            p("male", "♂");
            p("female", "♀");
            p("tm", "™");
            p("registered", "®");
            p("copyright", "©");
            p("toxic", "☣");
            p("yinyang", "☯");
            p("peace", "☮");
            if (variations) p("peace2", "✌");
        }
        if (this.math.get()) {
            p("diamond", "⋄");
            p("dot", "⋅");
            p("division_times", "⋇");
        }
        if (this.emoticons.get()) {
            p("smile", "☺");
            p("sad", "☹");
            p("skull", "☠");
            p("point_up", "☝");
            p("point_down", "☟");
            p("writing", "✍");
            p("point_right", "☞");
            p("point_left", "☜");
            if (variations) {
                p("smile2", "☻");
                p("smile3", "ツ");
                p("point_left2", "☚");
                p("point_right2", "☛");
            }
        }
        if (this.games.get()) {
            p("black_king", "♔");
            p("black_queen", "♕");
            p("white_king", "♚");
            p("white_queen", "♛");
            p("spade", "♠");
            p("spade2", "♤");
            p("club", "♣");
            p("club2", "♧");
        }
        if (this.miscellanious.get()) {
            p("heart", "❤");
            p("star", "★");
            p("flower", "❀");
            p("cat", "ᓚᘏᗢ");
            p("wave", "🌊");
            p("sun", "☀");
            p("pencil", "✎");
            p("umbrella", "☂");
            p("sparkle", "❈");
            p("cross", "✖");
            p("check", "✓");
            p("scissors", "✄");
            p("telephone", "☎");
            p("phone", "✆");
            p("mail", "✉");
            p("black_square", "□");
            p("white_square", "■");
            p("triple_bar", "≡");
            p("communism", "⚒");
            p("quebec", "⚜");
            if (variations) {
                p("heart2", "❥");
                p("heart3", "♥");
                p("heart4", "♡");
                p("heart5", "❣");
                p("star2", "☆");
                p("star3", "✮");
                p("star4", "✪");
                p("star5", "✦");
                p("star6", "✧");
                p("star7", "⋆");
                p("flower2", "✿");
                p("sun2", "☼");
                p("cross2", "✗");
                p("cross3", "✘");
                p("cross4", "✕");
                p("check2", "✓");
                p("telephone2", "☏");
            }
        }
    }
}
