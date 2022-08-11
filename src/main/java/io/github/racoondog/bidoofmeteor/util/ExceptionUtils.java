package io.github.racoondog.bidoofmeteor.util;

import io.github.racoondog.bidoofmeteor.BidoofMeteor;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.util.ExceptionUtil;

@Environment(EnvType.CLIENT)
public class ExceptionUtils {
    public static void throwException(Throwable t, String cause) {
        RuntimeException exception = ExceptionUtil.gatherExceptions(t, null, exc -> new RuntimeException(String.format(cause), exc));

        if (exception != null) {
            throw exception;
        }
    }

    public static String packageFallback(MeteorAddon addon, AbstractMethodError e) {
        try {
            String packageName = addon.getClass().getPackageName();
            BidoofMeteor.LOG.error("'%s' does not support the current version of Meteor Client and needs to be updated.\nHowever, a fallback has been used to allow the mod to run.".formatted(addon.name));
            return packageName;
        } catch (Throwable t) {
            ExceptionUtils.throwException(e, "'%s' does not support the current version of Meteor Client and needs to be updated.".formatted(addon.name));
        }
        return null;
    }
}
