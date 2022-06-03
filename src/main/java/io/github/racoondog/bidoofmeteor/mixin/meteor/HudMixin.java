package io.github.racoondog.bidoofmeteor.mixin.meteor;

import io.github.racoondog.bidoofmeteor.hud.BidoofImageHud;
import meteordevelopment.meteorclient.systems.hud.HUD;
import meteordevelopment.meteorclient.systems.hud.modules.HudElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(value = HUD.class, remap = false)
public abstract class HUDMixin {
    @Shadow
    @Final
    public List<HudElement> elements;

    /**
     * @author Crosby
     * Handles creating the appropriate amount of BidoofImageHud objects on load
     */
    @Inject(method = "fromTag(Lnet/minecraft/nbt/NbtCompound;)Lmeteordevelopment/meteorclient/systems/hud/HUD;", at = @At("HEAD"))
    private void inject(NbtCompound tag, CallbackInfoReturnable<HUD> cir) {
        if (!tag.contains("elements")) return;
        NbtList elementsTag = tag.getList("elements", 10);
        for (NbtElement t : elementsTag) {
            NbtCompound elementTag = (NbtCompound) t;
            if (!elementTag.contains("name")) return;
            String name = elementTag.getString("name");
            if (name.startsWith("bidoof-image")) elements.add(new BidoofImageHud());
        }
    }
}
