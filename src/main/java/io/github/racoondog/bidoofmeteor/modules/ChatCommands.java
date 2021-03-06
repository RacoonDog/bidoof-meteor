package io.github.racoondog.bidoofmeteor.modules;

import io.github.racoondog.bidoofmeteor.BidoofMeteor;
import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.world.TickRate;
import meteordevelopment.orbit.EventHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.RandomSeed;

import static io.github.racoondog.bidoofmeteor.BidoofStarscript.*;
import static io.github.racoondog.bidoofmeteor.util.ChatUtils.say;

@Environment(EnvType.CLIENT)
public class ChatCommands extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<Boolean> time = sgGeneral.add(new BoolSetting.Builder()
        .name("time")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> tps = sgGeneral.add(new BoolSetting.Builder()
        .name("tps")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> pos = sgGeneral.add(new BoolSetting.Builder()
        .name("position")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> fakepos = sgGeneral.add(new BoolSetting.Builder()
        .name("fake-position")
        .defaultValue(true)
        .visible(pos::get)
        .build()
    );

    private final Setting<Boolean> random = sgGeneral.add(new BoolSetting.Builder()
        .name("random")
        .defaultValue(true)
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("The delay between specified messages in ticks.")
        .defaultValue(20)
        .min(0)
        .sliderMax(200)
        .build()
    );

    private static final Random RANDOM = new LocalRandom(RandomSeed.getSeed());

    private int timer;

    public ChatCommands() {
        super(BidoofMeteor.CATEGORY, "chat-commands", "Chat commands.");
    }

    @Override
    public void onActivate() {
        timer = delay.get();
    }

    @EventHandler
    private void onMessageReceive(ReceiveMessageEvent event) {
        if (timer > 0) return;
        String message = event.getMessage().getString();
        if (message.contains("!tps") && tps.get()) say("Current TPS: " + TickRate.INSTANCE.getTickRate());
        else if (message.contains("!time") && time.get()) say("It's currently " + Utils.getWorldTime());
        else if (message.contains("!pos") && pos.get()) {
            if (!fakepos.get()) {
                say(String.format("Pos: %.0f, %.0f, %.0f", mc.player.getX(), mc.player.getY(), mc.player.getZ()));
            } else {
                say(String.format("Pos: %.0f, %.0f, %.0f", mc.player != null ? fakeX + mc.player.getX(): fakeX, mc.player != null ? fakeY + mc.player.getY() : fakeY, mc.player != null ? fakeZ + mc.player.getZ() : fakeZ));
            }
        } else if (random.get()) {
            if (message.contains("!roll")) say("You rolled %s/100!".formatted(RANDOM.nextInt(101)));
            else if (message.contains("!d4")) say("You rolled %s!".formatted(RANDOM.nextInt(4) + 1));
            else if (message.contains("!d6")) say("You rolled %s!".formatted(RANDOM.nextInt(6) + 1));
            else if (message.contains("!d8")) say("You rolled %s!".formatted(RANDOM.nextInt(8) + 1));
            else if (message.contains("!d10")) say("You rolled %s!".formatted(RANDOM.nextInt(10) + 1));
            else if (message.contains("!d12")) say("You rolled %s!".formatted(RANDOM.nextInt(12) + 1));
            else if (message.contains("!d20")) say("You rolled %s!".formatted(RANDOM.nextInt(20) + 1));
            else if (message.contains("!d100")) say("You rolled %s!".formatted(RANDOM.nextInt(100) + 1));
        }
        timer = delay.get();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (timer > 0) timer--;
    }

}
