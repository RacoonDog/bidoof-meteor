package io.github.racoondog.bidoofmeteor.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.racoondog.bidoofmeteor.util.PlayerUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.CoordinateArgument;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

//bla bla bla MIT license bla bla bla sauce: https://github.com/xpple/clientarguments
@Environment(EnvType.CLIENT)
public record DefaultPosArgument(CoordinateArgument x, CoordinateArgument y,
                                 CoordinateArgument z) implements PosArgument {

    public Vec3d toAbsolutePos(CommandSource source) {
        Vec3d vec3d = PlayerUtils.getPositionVec3d();
        return new Vec3d(this.x.toAbsoluteCoordinate(vec3d.x), this.y.toAbsoluteCoordinate(vec3d.y), this.z.toAbsoluteCoordinate(vec3d.z));
    }

    public Vec2f toAbsoluteRotation(CommandSource source) {
        Vec2f vec2f = PlayerUtils.getRotationVec2f();
        return new Vec2f((float) this.x.toAbsoluteCoordinate(vec2f.x), (float) this.y.toAbsoluteCoordinate(vec2f.y));
    }

    public boolean isXRelative() {
        return this.x.isRelative();
    }

    public boolean isYRelative() {
        return this.y.isRelative();
    }

    public boolean isZRelative() {
        return this.z.isRelative();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof DefaultPosArgument defaultPosArgument)) {
            return false;
        } else {
            if (this.x.equals(defaultPosArgument.x)) {
                return this.y.equals(defaultPosArgument.y) && this.z.equals(defaultPosArgument.z);
            }
            return false;
        }
    }

    public static DefaultPosArgument parse(StringReader reader) throws CommandSyntaxException {
        int cursor = reader.getCursor();
        CoordinateArgument coordinateArgument = CoordinateArgument.parse(reader);
        if (reader.canRead() && reader.peek() == ' ') {
            reader.skip();
            CoordinateArgument coordinateArgument2 = CoordinateArgument.parse(reader);
            if (reader.canRead() && reader.peek() == ' ') {
                reader.skip();
                CoordinateArgument coordinateArgument3 = CoordinateArgument.parse(reader);
                return new DefaultPosArgument(coordinateArgument, coordinateArgument2, coordinateArgument3);
            } else {
                reader.setCursor(cursor);
                throw Vec3ArgumentType.INCOMPLETE_EXCEPTION.createWithContext(reader);
            }
        } else {
            reader.setCursor(cursor);
            throw Vec3ArgumentType.INCOMPLETE_EXCEPTION.createWithContext(reader);
        }
    }

    public static DefaultPosArgument parse(StringReader reader, boolean centerIntegers) throws CommandSyntaxException {
        int cursor = reader.getCursor();
        CoordinateArgument coordinateArgument = CoordinateArgument.parse(reader, centerIntegers);
        if (reader.canRead() && reader.peek() == ' ') {
            reader.skip();
            CoordinateArgument coordinateArgument2 = CoordinateArgument.parse(reader, false);
            if (reader.canRead() && reader.peek() == ' ') {
                reader.skip();
                CoordinateArgument coordinateArgument3 = CoordinateArgument.parse(reader, centerIntegers);
                return new DefaultPosArgument(coordinateArgument, coordinateArgument2, coordinateArgument3);
            } else {
                reader.setCursor(cursor);
                throw Vec3ArgumentType.INCOMPLETE_EXCEPTION.createWithContext(reader);
            }
        } else {
            reader.setCursor(cursor);
            throw Vec3ArgumentType.INCOMPLETE_EXCEPTION.createWithContext(reader);
        }
    }

    public static DefaultPosArgument absolute(double x, double y, double z) {
        return new DefaultPosArgument(new CoordinateArgument(false, x), new CoordinateArgument(false, y), new CoordinateArgument(false, z));
    }

    public static DefaultPosArgument absolute(Vec2f vec) {
        return new DefaultPosArgument(new CoordinateArgument(false, (double) vec.x), new CoordinateArgument(false, (double) vec.y), new CoordinateArgument(true, 0.0D));
    }

    public static DefaultPosArgument zero() {
        return new DefaultPosArgument(new CoordinateArgument(true, 0.0D), new CoordinateArgument(true, 0.0D), new CoordinateArgument(true, 0.0D));
    }
}
