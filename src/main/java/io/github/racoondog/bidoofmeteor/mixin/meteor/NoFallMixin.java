package io.github.racoondog.bidoofmeteor.mixin.meteor;

import io.github.racoondog.bidoofmeteor.mixininterface.INoFall;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.movement.NoFall;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(NoFall.class)
public abstract class NoFallMixin extends Module implements INoFall {
    @Shadow @Final private Setting<NoFall.Mode> mode;

    public NoFallMixin(Category category, String name, String description) {
        super(category, name, description);
    }

    @Override
    public boolean isModeBlockPlace() {
        return this.isActive() && !this.mode.get().equals(NoFall.Mode.Packet);
    }
}
