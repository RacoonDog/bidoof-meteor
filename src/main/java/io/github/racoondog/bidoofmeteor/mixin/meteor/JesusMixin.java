package io.github.racoondog.bidoofmeteor.mixin.meteor;

import io.github.racoondog.bidoofmeteor.mixininterface.IJesus;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.systems.modules.movement.Jesus;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(Jesus.class)
public abstract class JesusMixin implements IJesus {
    @Shadow @Final private Setting<Jesus.Mode> waterMode;
    @Shadow @Final private Setting<Jesus.Mode> lavaMode;

    @Override
    public Jesus.Mode getWaterMode() {
        return this.waterMode.get();
    }

    @Override
    public Jesus.Mode getLavaMode() {
        return this.lavaMode.get();
    }
}
