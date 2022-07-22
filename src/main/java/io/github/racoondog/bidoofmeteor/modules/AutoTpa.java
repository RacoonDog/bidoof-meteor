package io.github.racoondog.bidoofmeteor.modules;

import io.github.racoondog.bidoofmeteor.BidoofMeteor;
import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringListSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.util.List;

@Environment(EnvType.CLIENT)
public class AutoTpa extends Module {
    public enum Mode {
        All,
        Blacklist,
        Whitelist
    }

    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<Enum<Mode>> mode = sgGeneral.add(new EnumSetting.Builder<Enum<Mode>>()
        .name("mode")
        .defaultValue(Mode.Whitelist)
        .build()
    );

    private final Setting<List<String>> list = sgGeneral.add(new StringListSetting.Builder()
        .name("list")
        .visible(() -> !mode.get().equals(Mode.All))
        .build()
    );

    public AutoTpa() {
        super(BidoofMeteor.CATEGORY, "auto-tpa", "Automatically accepts tpa requests.");
    }

    @EventHandler
    private void onMessageReceive(ReceiveMessageEvent event) {
        if (event.isModified() || event.isCancelled() || mc.player == null) return;
        Text text = event.getMessage();
        //if (!text.getStyle().isEmpty()) return; todo fix
        String message = text.getString();
        BidoofMeteor.LOG.info(message);
        int idx = message.indexOf(' ');
        if (idx == -1) return;
        String name = message.substring(0, idx);
        message = message.substring(idx);
        BidoofMeteor.LOG.info(message);
        if (!message.equals(" has requested to teleport to you.")) return;
        if (!mode.get().equals(Mode.All)) {
            BidoofMeteor.LOG.info(name);
            if (list.get().contains(name)) {
                if (mode.get().equals(Mode.Whitelist)) {
                    mc.player.sendCommand("tpaccept");
                }
            }
        } else {
            mc.player.sendCommand("tpaccept");
        }
    }
}
