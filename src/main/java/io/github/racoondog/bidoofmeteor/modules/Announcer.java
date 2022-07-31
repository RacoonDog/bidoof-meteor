package io.github.racoondog.bidoofmeteor.modules;

import io.github.racoondog.bidoofmeteor.BidoofMeteor;
import io.github.racoondog.bidoofmeteor.util.Constants;
import io.github.racoondog.bidoofmeteor.util.StarscriptUtils;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.entity.DropItemsEvent;
import meteordevelopment.meteorclient.events.entity.player.BreakBlockEvent;
import meteordevelopment.meteorclient.events.entity.player.PickItemsEvent;
import meteordevelopment.meteorclient.events.entity.player.PlaceBlockEvent;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.gui.utils.StarscriptTextBoxRenderer;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.misc.MeteorStarscript;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.starscript.Script;
import meteordevelopment.starscript.utils.StarscriptError;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.Item;

@Environment(EnvType.CLIENT)
public class Announcer extends Module {
    private final Feature[] features = {
        new Moving(),
        new Mining(),
        new Placing(),
        new DropItems(),
        new PickItems(),
        new OpenContainer()
    };

    public Announcer() {
        super(BidoofMeteor.CATEGORY, "announcer", "Announces specified actions into chat.");
    }

    @Override
    public void onActivate() {
        for (Feature feature : features) {
            if (feature.isEnabled()) {
                MeteorClient.EVENT_BUS.subscribe(feature);
                feature.reset();
            }
        }
    }

    @Override
    public void onDeactivate() {
        for (Feature feature : features) {
            if (feature.isEnabled()) {
                MeteorClient.EVENT_BUS.unsubscribe(feature);
            }
        }
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        for (Feature feature : features) {
            if (feature.isEnabled()) feature.tick();
        }
    }

    private abstract class Feature {
        protected SettingGroup sg;

        private final Setting<Boolean> enabled;

        protected Feature(String name, String enabledName, String enabledDescription) {
            this.sg = settings.createGroup(name);

            enabled = sg.add(new BoolSetting.Builder()
                .name(enabledName)
                .description(enabledDescription)
                .defaultValue(true)
                .onChanged(aBoolean -> {
                    if (isActive() && isEnabled()) {
                        MeteorClient.EVENT_BUS.subscribe(this);
                        reset();
                    } else if (isActive() && !isEnabled()) {
                        MeteorClient.EVENT_BUS.unsubscribe(this);
                    }
                })
                .build()
            );
        }

        protected abstract void reset();

        protected abstract void tick();

        boolean isEnabled() {
            return enabled.get();
        }
    }

    private class Moving extends Feature {
        private final Setting<String> msg = sg.add(new StringSetting.Builder()
            .name("moving-msg")
            .description("The chat message for moving a certain amount of blocks.")
            .defaultValue("I just moved [dist] blocks!")
            .visible(this::isEnabled)
            .onChanged(e -> compile())
            .renderer(StarscriptTextBoxRenderer.class)
            .build()
        );

        private final Setting<Double> delay = sg.add(new DoubleSetting.Builder()
            .name("moving-delay")
            .description("The amount of delay between moving messages in seconds.")
            .defaultValue(10)
            .sliderMax(60)
            .visible(this::isEnabled)
            .build()
        );

        private final Setting<Double> minDist = sg.add(new DoubleSetting.Builder()
            .name("moving-min-dist")
            .description("The minimum distance for a moving message to send into chat.")
            .defaultValue(10)
            .sliderMax(100)
            .visible(this::isEnabled)
            .build()
        );

        private double dist, timer;
        private double lastX, lastZ;
        private boolean first;
        private Script script;

        Moving() {
            super("Moving", "moving-enabled", "Send msg how much you moved.");
            compile();
        }

        @Override
        protected void reset() {
            dist = 0;
            timer = 0;
            first = true;
        }

        @Override
        protected void tick() {
            if (first) {
                first = false;
                updateLastPos();
            }

            double deltaX = mc.player.getX() - lastX;
            double deltaZ = mc.player.getZ() - lastZ;
            dist += Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

            if (timer >= delay.get()) {
                timer = 0;

                if (dist >= minDist.get()) {
                    sendMsg();
                    dist = 0;
                }
            } else {
                timer += Constants.TICK;
            }

            updateLastPos();
        }

        private void compile() {
            script = StarscriptUtils.compile(msg.get());
        }

        private void updateLastPos() {
            lastX = mc.player.getX();
            lastZ = mc.player.getZ();
        }

        private void sendMsg() {
            if (script == null) return;
            try {
                ChatUtils.sendPlayerMsg(MeteorStarscript.ss.run(script).toString().replace("[dist]", String.format("%.1f", dist)));
            } catch (StarscriptError error) {
                MeteorStarscript.printChatError(error);
            }
        }
    }

    private class Mining extends Feature {
        private final Setting<String> msg = sg.add(new StringSetting.Builder()
            .name("mining-msg")
            .description("The chat message for mining blocks.")
            .defaultValue("I just mined [count] [block]!")
            .visible(this::isEnabled)
            .onChanged(e -> compile())
            .renderer(StarscriptTextBoxRenderer.class)
            .build()
        );

        private Block lastBlock;
        private int count;
        private double notBrokenTimer;
        private Script script;

        Mining() {
            super("Mining", "mining-enabled", "Send msg how much blocks you mined.");
            compile();
        }

        @Override
        protected void reset() {
            lastBlock = null;
            count = 0;
            notBrokenTimer = 0;
        }

        @EventHandler
        private void onBreakBlock(BreakBlockEvent event) {
            Block block = event.getBlockState(mc.world).getBlock();

            if (lastBlock != null && lastBlock != block) {
                sendMsg();
            }

            lastBlock = block;
            count++;
            notBrokenTimer = 0;
        }

        @Override
        protected void tick() {
            if (notBrokenTimer >= 2) {
                sendMsg();
            } else {
                notBrokenTimer += Constants.TICK;
            }
        }

        private void compile() {
            script = StarscriptUtils.compile(msg.get());
        }

        private void sendMsg() {
            if (script == null) return;
            if (count > 0) {
                try {
                    ChatUtils.sendPlayerMsg(MeteorStarscript.ss.run(script).toString().replace("[count]", Integer.toString(count)).replace("[block]", lastBlock.getName().getString()));
                } catch (StarscriptError error) {
                    MeteorStarscript.printChatError(error);
                }
                count = 0;
            }
        }
    }

    private class Placing extends Feature {
        private final Setting<String> msg = sg.add(new StringSetting.Builder()
            .name("placing-msg")
            .description("The chat message for placing blocks.")
            .defaultValue("I just placed [count] [block]!")
            .visible(this::isEnabled)
            .onChanged(e -> compile())
            .renderer(StarscriptTextBoxRenderer.class)
            .build()
        );

        private Block lastBlock;
        private int count;
        private double notPlacedTimer;
        private Script script;

        Placing() {
            super("Placing", "placing-enabled", "Send msg how much blocks you placed.");
            compile();
        }

        @Override
        protected void reset() {
            lastBlock = null;
            count = 0;
            notPlacedTimer = 0;
        }

        @EventHandler
        private void onPlaceBlock(PlaceBlockEvent event) {
            if (lastBlock != null && lastBlock != event.block) {
                sendMsg();
            }

            lastBlock = event.block;
            count++;
            notPlacedTimer = 0;
        }

        @Override
        protected void tick() {
            if (notPlacedTimer >= 2) {
                sendMsg();
            } else {
                notPlacedTimer += Constants.TICK;
            }
        }

        private void compile() {
            script = StarscriptUtils.compile(msg.get());
        }

        private void sendMsg() {
            if (script == null) return;
            if (count > 0) {
                try {
                    ChatUtils.sendPlayerMsg(MeteorStarscript.ss.run(script).toString().replace("[count]", Integer.toString(count)).replace("[block]", lastBlock.getName().getString()));
                } catch (StarscriptError error) {
                    MeteorStarscript.printChatError(error);
                }
                count = 0;
            }
        }
    }

    private class DropItems extends Feature {
        private final Setting<String> msg = sg.add(new StringSetting.Builder()
            .name("drop-items-msg")
            .description("The chat message for dropping items.")
            .defaultValue("I just dropped [count] [item]!")
            .visible(this::isEnabled)
            .onChanged(e -> compile())
            .renderer(StarscriptTextBoxRenderer.class)
            .build()
        );

        private Item lastItem;
        private int count;
        private double notDroppedTimer;
        private Script script;

        DropItems() {
            super("Drop Items", "drop-items-enabled", "Send msg how much items you dropped.");
            compile();
        }

        @Override
        protected void reset() {
            lastItem = null;
            count = 0;
            notDroppedTimer = 0;
        }

        @EventHandler
        private void onDropItems(DropItemsEvent event) {
            if (lastItem != null && lastItem != event.itemStack.getItem()) {
                sendMsg();
            }

            lastItem = event.itemStack.getItem();
            count += event.itemStack.getCount();
            notDroppedTimer = 0;
        }

        @Override
        protected void tick() {
            if (notDroppedTimer >= 1) {
                sendMsg();
            } else {
                notDroppedTimer += Constants.TICK;
            }
        }

        private void compile() {
            script = StarscriptUtils.compile(msg.get());
        }

        private void sendMsg() {
            if (script == null) return;
            if (count > 0) {
                try {
                    ChatUtils.sendPlayerMsg(MeteorStarscript.ss.run(script).toString().replace("[count]", Integer.toString(count)).replace("[item]", lastItem.getName().getString()));
                } catch (StarscriptError error) {
                    MeteorStarscript.printChatError(error);
                }
                count = 0;
            }
        }
    }

    private class PickItems extends Feature {
        private final Setting<String> msg = sg.add(new StringSetting.Builder()
            .name("pick-items-msg")
            .description("The chat message for picking up items.")
            .defaultValue("I just picked up [count] [item]!")
            .visible(this::isEnabled)
            .onChanged(e -> compile())
            .renderer(StarscriptTextBoxRenderer.class)
            .build()
        );

        private Item lastItem;
        private int count;
        private double notPickedUpTimer;
        private Script script;

        PickItems() {
            super("Pick Items", "pick-items-enabled", "Send msg how much items you pick up.");
            compile();
        }

        @Override
        protected void reset() {
            lastItem = null;
            count = 0;
            notPickedUpTimer = 0;
        }

        @EventHandler
        private void onPickItems(PickItemsEvent event) {
            if (lastItem != null && lastItem != event.itemStack.getItem()) {
                sendMsg();
            }

            lastItem = event.itemStack.getItem();
            count += event.itemStack.getCount();
            notPickedUpTimer = 0;
        }

        @Override
        protected void tick() {
            if (notPickedUpTimer >= 1) {
                sendMsg();
            } else {
                notPickedUpTimer += Constants.TICK;
            }
        }

        private void compile() {
            script = StarscriptUtils.compile(msg.get());
        }

        private void sendMsg() {
            if (script == null) return;
            if (count > 0) {
                try {
                    ChatUtils.sendPlayerMsg(MeteorStarscript.ss.run(script).toString().replace("[count]", Integer.toString(count)).replace("[item]", lastItem.getName().getString()));
                } catch (StarscriptError error) {
                    MeteorStarscript.printChatError(error);
                }
                count = 0;
            }
        }
    }

    private class OpenContainer extends Feature {
        private final Setting<String> msg = sg.add(new StringSetting.Builder()
            .name("open-container-msg")
            .description("The chat message for opening a container.")
            .defaultValue("I just opened [name]!")
            .visible(this::isEnabled)
            .onChanged(e -> compile())
            .renderer(StarscriptTextBoxRenderer.class)
            .build()
        );

        private Script script;

        public OpenContainer() {
            super("Open Container", "open-container-enabled", "Sends msg when you open containers.");
            compile();
        }

        @Override
        protected void reset() {}

        @Override
        protected void tick() {}

        @EventHandler
        private void onOpenScreen(OpenScreenEvent event) {
            if (event.screen instanceof HandledScreen<?>) {
                String name = event.screen.getTitle().getString();
                if (!name.isEmpty()) sendMsg(name);
            }
        }

        private void compile() {
            script = StarscriptUtils.compile(msg.get());
        }

        void sendMsg(String name) {
            if (script == null) return;
            try {
                ChatUtils.sendPlayerMsg(MeteorStarscript.ss.run(script).toString().replace("[name]", name));
            } catch (StarscriptError error) {
                MeteorStarscript.printChatError(error);
            }
        }
    }
}
