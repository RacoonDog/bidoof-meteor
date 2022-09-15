package io.github.racoondog.bidoofmeteor.modules;

import io.github.racoondog.bidoofmeteor.BidoofMeteor;
import io.github.racoondog.bidoofmeteor.impl.FishyDetectorImpl;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.containers.WSection;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.containers.WVerticalList;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class FishyDetector extends Module {
    public FishyDetector() {
        super(BidoofMeteor.CATEGORY, "fishy-detector", "Detects anomalies in players.");
    }

    public static boolean active() {
        return Modules.get().get(FishyDetector.class).isActive();
    }

    @Override
    public WWidget getWidget(GuiTheme theme) {
        WVerticalList list = theme.verticalList();

        WTable table = list.add(theme.table()).expandX().widget();

        WButton button = table.add(theme.button("Detect Fishy")).expandX().widget();
        button.action = FishyDetectorImpl::detectFishy;

        return list;
    }
}
