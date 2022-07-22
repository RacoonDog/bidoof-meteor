package io.github.racoondog.bidoofmeteor.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class PlayerUtils {
    public static Vec3d getPositionVec3d() {
        assert mc.player != null;
        BlockPos pos = mc.player.getBlockPos();
        return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Vec2f getRotationVec2f() {
        assert mc.player != null;
        return mc.player.getRotationClient();
    }
}
