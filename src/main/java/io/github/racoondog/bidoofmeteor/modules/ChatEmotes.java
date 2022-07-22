package io.github.racoondog.bidoofmeteor.modules;

import io.github.racoondog.bidoofmeteor.BidoofMeteor;
import io.github.racoondog.bidoofmeteor.impl.ChatEmotesImpl;
import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.events.game.SendMessageEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ChatEmotes extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final SettingGroup sgEmoteGroups = this.settings.createGroup("emote-groups");

    private final Setting<Boolean> outgoing = sgGeneral.add(new BoolSetting.Builder()
        .name("outgoing")
        .description("Applies emotes on outgoing chat messages.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> incoming = sgGeneral.add(new BoolSetting.Builder()
        .name("incoming")
        .description("Applies emotes on incoming chat messages.")
        .defaultValue(false)
        .build()
    );

    public final Setting<Boolean> variations = sgGeneral.add(new BoolSetting.Builder()
        .name("variations")
        .description("Enables multiple variations of the same chat emote.")
        .defaultValue(true)
        .build()
    );

    public final Setting<Boolean> minecraft = sgEmoteGroups.add(new BoolSetting.Builder()
        .name("minecraft")
        .defaultValue(true)
        .build()
    );

    public final Setting<Boolean> alphabet = sgEmoteGroups.add(new BoolSetting.Builder()
        .name("alphabet")
        .defaultValue(true)
        .build()
    );

    public final Setting<Boolean> music = sgEmoteGroups.add(new BoolSetting.Builder()
        .name("music")
        .defaultValue(true)
        .build()
    );

    public final Setting<Boolean> symbols = sgEmoteGroups.add(new BoolSetting.Builder()
        .name("symbols")
        .defaultValue(true)
        .build()
    );

    public final Setting<Boolean> math = sgEmoteGroups.add(new BoolSetting.Builder()
        .name("math")
        .defaultValue(true)
        .build()
    );

    public final Setting<Boolean> emoticons = sgEmoteGroups.add(new BoolSetting.Builder()
        .name("emoticons")
        .defaultValue(true)
        .build()
    );

    public final Setting<Boolean> games = sgEmoteGroups.add(new BoolSetting.Builder()
        .name("games")
        .defaultValue(true)
        .build()
    );

    public final Setting<Boolean> miscellanious = sgEmoteGroups.add(new BoolSetting.Builder()
        .name("miscellanious")
        .defaultValue(true)
        .build()
    );

    public ChatEmotes() {
        super(BidoofMeteor.CATEGORY, "chat-emotes", "Enables the usage of emotes in the chat.");
    }

    @EventHandler
    private void onMessageSend(SendMessageEvent event) {
        if (!outgoing.get()) return;
        event.message = ChatEmotesImpl.emoteReplacer(event.message);
    }

    @EventHandler
    private void onMessageReceive(ReceiveMessageEvent event) {
        if (!incoming.get()) return;
        event.setMessage(Text.literal(ChatEmotesImpl.emoteReplacer(event.getMessage().getString())));
    }
}
