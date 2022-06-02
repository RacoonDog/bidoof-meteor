package io.github.racoondog.bidoofmeteor.mixin.meteor;

import io.github.racoondog.bidoofmeteor.BidoofMeteor;
import meteordevelopment.meteorclient.systems.hud.HUD;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = HUD.class, remap = false)
public class HudMixin {
    @Inject(method = "toTag", at = @At("RETURN"))
    private void e(CallbackInfoReturnable<NbtCompound> cir) {
        BidoofMeteor.LOG.info(cir.getReturnValue().asString());
    }
}
