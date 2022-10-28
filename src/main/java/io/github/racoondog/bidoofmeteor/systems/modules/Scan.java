package io.github.racoondog.bidoofmeteor.systems.modules;

import io.github.racoondog.bidoofmeteor.BidoofMeteor;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.containers.WVerticalList;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class Scan extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    public final Setting<Boolean> scanOnWorldJoin = sgGeneral.add(new BoolSetting.Builder()
        .name("scan-on-world-join")
        .description("Automatically scan on world join.")
        .defaultValue(false)
        .build()
    );

    public final Setting<Boolean> scanOnServerJoin = sgGeneral.add(new BoolSetting.Builder()
        .name("scan-on-server-join")
        .description("Automatically scan on server join.")
        .defaultValue(true)
        .build()
    );

    public Scan() {
        super(BidoofMeteor.CATEGORY, "scan", "Scan worlds and servers for known exploits.");
    }

    @Override
    public WWidget getWidget(GuiTheme theme) {
        WVerticalList list = theme.verticalList();

        WTable table = list.add(theme.table()).expandX().widget();

        WButton button = table.add(theme.button("Scan")).expandX().widget();
        button.action = () -> {

        };

        return list;
    }
}
