package io.github.racoondog.bidoofmeteor.mixin;

import io.github.racoondog.bidoofmeteor.modules.CommandSubstituter;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
    @Inject(method = "sendCommand(Ljava/lang/String;)Z", at = @At("HEAD"))
    private void modifyArg(String command, CallbackInfoReturnable<Boolean> cir) {
        if (!Modules.get().get(CommandSubstituter.class).isActive()) return;
        int idx = command.indexOf(' ');
        if (idx == -1) return;
        String str = command.substring(0, idx);
        str = Modules.get().get(CommandSubstituter.class).commandSubstitutions.getOrDefault(str, str);
        command =  str + command.substring(idx);
    }
}
