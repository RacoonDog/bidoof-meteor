package io.github.racoondog.bidoofmeteor.commands.arguments;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

//bla bla bla MIT license bla bla bla sauce: https://github.com/xpple/clientarguments
@Environment(EnvType.CLIENT)
public interface PosArgument {
    Vec3d toAbsolutePos(CommandSource source);

    Vec2f toAbsoluteRotation(CommandSource source);

    default BlockPos toAbsoluteBlockPos(CommandSource source) {
        return new BlockPos(this.toAbsolutePos(source));
    }

    boolean isXRelative();

    boolean isYRelative();

    boolean isZRelative();
}
