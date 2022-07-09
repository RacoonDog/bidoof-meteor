package io.github.racoondog.bidoofmeteor.mixin;

import com.mojang.brigadier.StringReader;
import io.github.racoondog.bidoofmeteor.mixininterface.IStringReader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(value = StringReader.class, remap = false)
public abstract class StringReaderMixin implements IStringReader {
    @Shadow private int cursor;
    @Shadow public abstract boolean canRead();
    @Shadow public abstract char peek();
    @Shadow public abstract void skip();
    @Shadow @Final private String string;

    @Unique
    private static boolean isAllowedInUsername(final char c) {
        return c >= '0' && c <= '9'
            || c >= 'A' && c <= 'Z'
            || c >= 'a' && c <= 'z'
            || c == '_';
    }

    @Unique
    private static boolean isAllowedInUUID(final char c) {
        return c >= '0' && c <= '9'
            || c >= 'A' && c <= 'F'
            || c >= 'a' && c <= 'f'
            || c == '-';
    }

    @Override
    public String readUsername() {
        final int start = cursor;
        while (canRead() && isAllowedInUsername(peek())) {
            skip();
        }
        return string.substring(start, cursor);
    }

    @Override
    public String readUUID() {
        final int start = cursor;
        while (canRead() && isAllowedInUUID(peek())) {
            skip();
        }
        return string.substring(start, cursor);
    }
}
