package io.github.racoondog.bidoofmeteor.modules;

import io.github.racoondog.bidoofmeteor.BidoofMeteor;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringListSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.HashMap;
import java.util.List;

@Environment(EnvType.CLIENT)
public class CommandSubstituter extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<List<String>> substitutions = sgGeneral.add(new StringListSetting.Builder()
        .name("substitutions")
        .description("Original and substituted separated with a comma.")
        .defaultValue("r,er")
        .onChanged(e -> modifyMap())
        .build()
    );

    public final HashMap<String, String> commandSubstitutions = new HashMap<>();

    public CommandSubstituter() {
        super(BidoofMeteor.CATEGORY, "command-substituter", "Automatically substitutes specified commands.");
        modifyMap();
    }

    private void modifyMap() {
        commandSubstitutions.clear();
        for (var string : substitutions.get()) {
            try {
                String[] strings = string.split(",");
                commandSubstitutions.put(strings[0], strings[1]);
            } catch (Exception e) {}
        }
    }
}
