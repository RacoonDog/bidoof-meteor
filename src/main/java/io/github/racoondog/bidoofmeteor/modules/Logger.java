package io.github.racoondog.bidoofmeteor.modules;

import io.github.racoondog.bidoofmeteor.BidoofMeteor;
import io.github.racoondog.bidoofmeteor.util.ChatUtils;
import io.github.racoondog.bidoofmeteor.util.StarscriptUtils;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.gui.utils.StarscriptTextBoxRenderer;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.misc.MeteorStarscript;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.starscript.Script;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.BlockPos;

import static io.github.racoondog.bidoofmeteor.BidoofMeteor.LOG;

@Environment(EnvType.CLIENT)
public class Logger extends Module {
    private final SettingGroup sgTeleport = this.settings.createGroup("Teleport");
    private final SettingGroup sgDisconnect = this.settings.createGroup("Disconnect");
    private final SettingGroup sgDeath = this.settings.createGroup("Death");

    private final Setting<Boolean> teleport = this.sgTeleport.add(new BoolSetting.Builder()
        .name("teleport")
        .description("Logs info on teleport.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Integer> minimumDistance = this.sgTeleport.add(new IntSetting.Builder()
        .name("minimum-distance")
        .description("Minimum distance travelled for movement to be considered teleportation.")
        .noSlider()
        .defaultValue(1000)
        .visible(teleport::get)
        .build()
    );

    private final Setting<String> teleportMessage = this.sgTeleport.add(new StringSetting.Builder()
        .name("output-message")
        .defaultValue("Teleport detected; Server: {server}, Position: {player.pos}")
        .onChanged(s -> recompileTeleport())
        .visible(teleport::get)
        .renderer(StarscriptTextBoxRenderer.class)
        .build()
    );

    private final Setting<Boolean> teleportChatOutput = this.sgTeleport.add(new BoolSetting.Builder()
        .name("output-in-chat")
        .defaultValue(false)
        .visible(teleport::get)
        .build()
    );

    private final Setting<Boolean> disconnect = this.sgDisconnect.add(new BoolSetting.Builder()
        .name("disconnect")
        .description("Logs info on disconnect.")
        .defaultValue(false)
        .build()
    );

    private final Setting<String> disconnectMessage = this.sgDisconnect.add(new StringSetting.Builder()
        .name("output-message")
        .defaultValue("Disconnect detected; Server: {server}, Position: {player.pos}")
        .onChanged(s -> recompileDisconnect())
        .visible(disconnect::get)
        .renderer(StarscriptTextBoxRenderer.class)
        .build()
    );

    private final Setting<Boolean> death = this.sgDeath.add(new BoolSetting.Builder()
        .name("death")
        .description("Logs info on death.")
        .defaultValue(true)
        .build()
    );

    private final Setting<String> deathMessage = this.sgDeath.add(new StringSetting.Builder()
        .name("output-message")
        .defaultValue("Death detected; Server: {server}, Position: {player.pos}")
        .onChanged(s -> recompileDeath())
        .visible(death::get)
        .renderer(StarscriptTextBoxRenderer.class)
        .build()
    );

    private final Setting<Boolean> deathChatOutput = this.sgDeath.add(new BoolSetting.Builder()
        .name("output-in-chat")
        .defaultValue(true)
        .visible(death::get)
        .build()
    );

    private Script teleportScript;
    private Script disconnectScript;
    private Script deathScript;

    private boolean forceUpdate;
    private int tickCounter;
    private BlockPos pos;
    private boolean deathLogged;

    public Logger() {
        super(BidoofMeteor.CATEGORY, "logger", "Outputs a message when certain events happen.");
    }

    private void recompileTeleport() {
        this.teleportScript = StarscriptUtils.compile(this.teleportMessage.get());
        this.forceUpdate = true;
    }

    private void recompileDisconnect() {
        this.disconnectScript = StarscriptUtils.compile(this.disconnectMessage.get());
    }

    private void recompileDeath() {
        this.deathScript = StarscriptUtils.compile(this.deathMessage.get());
    }

    private static int distance(BlockPos one, BlockPos two) {
        int dx = one.getX() - two.getX();
        int dz = one.getZ() - two.getZ();
        return dx * dx + dz * dz;
    }

    private static BlockPos copy(BlockPos pos) {
        return new BlockPos(pos.getX(), pos.getY(), pos.getZ());
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        //Teleport
        if (mc.player == null) return;

        if (this.teleport.get() && mc.player.getBlockPos() != null && this.teleportScript != null) {
            if (this.tickCounter >= 20 || this.forceUpdate) {
                int minDistance = this.minimumDistance.get(); //comparing squared values is faster
                if (this.pos != null && distance(this.pos, mc.player.getBlockPos()) >= (minDistance * minDistance)) {
                    String output = MeteorStarscript.run(this.teleportScript);
                    if (output != null) {
                        if (this.teleportChatOutput.get()) {
                            ChatUtils.info(output);
                        }
                        LOG.info(output);
                    }
                }
                this.pos = copy(mc.player.getBlockPos());
                this.tickCounter = 0;
            }
            this.tickCounter++;
            this.forceUpdate = false;
        }

        //Death

        if (this.death.get() && this.deathScript != null) {
            if (mc.player.getHealth() <= 0.0f) {
                if (!this.deathLogged) {
                    this.deathLogged = true;
                    String output = MeteorStarscript.run(this.deathScript);
                    if (output != null) {
                        if (this.deathChatOutput.get()) ChatUtils.info(output);
                        LOG.info(output);
                    }
                }
            } else {
                this.deathLogged = false;
            }
        }
    }

    @EventHandler
    private void onDisconnect(GameLeftEvent event) {
        if (!this.disconnect.get() || this.disconnectScript == null) return;

        String output = MeteorStarscript.run(this.disconnectScript);
        if (output != null) LOG.info(output);
    }
}
