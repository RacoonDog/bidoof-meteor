package io.github.racoondog.bidoofmeteor.mixin;

import io.github.racoondog.bidoofmeteor.modules.CommandSubstituter;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
    @ModifyArg(method = "sendCommand(Ljava/lang/String;Lnet/minecraft/text/Text;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendCommand(Lnet/minecraft/network/message/ChatMessageSigner;Ljava/lang/String;Lnet/minecraft/text/Text;)V"), index = 1)
    private String modifyArg(String command) {
        if (!Modules.get().get(CommandSubstituter.class).isActive()) return command;
        int idx = command.indexOf(' ');
        if (idx == -1) return command;
        String str = command.substring(0, idx);
        str = Modules.get().get(CommandSubstituter.class).commandSubstitutions.getOrDefault(str, str);
        return str + command.substring(idx);
    }
}
