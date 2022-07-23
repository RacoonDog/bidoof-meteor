package io.github.racoondog.bidoofmeteor.mixin.meteor;

import io.github.racoondog.bidoofmeteor.themes.IRecolorGuiTheme;
import meteordevelopment.meteorclient.gui.themes.meteor.MeteorGuiTheme;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Environment(EnvType.CLIENT)
@Mixin(value = MeteorGuiTheme.class, remap = false)
public abstract class MeteorGuiThemeMixin {
    // Colors

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=accent")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 2)
    private SettingColor accentColor(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getAccentColor();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=checkbox")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 2)
    private SettingColor checkboxColor(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getCheckboxColor();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=plus")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 2)
    private SettingColor plusColor(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getPlusColor();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=minus")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 2)
    private SettingColor minusColor(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getMinusColor();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=favorite")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 2)
    private SettingColor favoriteColor(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getFavoriteColor();
        }
        return color;
    }

    // Text

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=text")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor textColor(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getTextColor();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=text-secondary-text")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor textSecondaryColor(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getTextSecondaryColor();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=text-highlight")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor textHighlightColor(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getTextHighlightColor();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=title-text")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor titleTextColor(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getTitleTextColor();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=logged-in-text")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor loggedInColor(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getLoggedInColor();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=placeholder")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor placeholderColor(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getPlaceholderColor();
        }
        return color;
    }

    // Background

    @ModifyArgs(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=background")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme$ThreeStateColorSetting;<init>(Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)V", ordinal = 0))
    private void backgroundColor(Args args) {
        if (args.get(0) instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            IRecolorGuiTheme.TriColorSetting triColorSetting = iRecolorGuiTheme.getBackgroundColor();
            args.set(3, triColorSetting.c1());
            args.set(4, triColorSetting.c2());
            args.set(5, triColorSetting.c3());
        }
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=module-background")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor moduleBackground(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getModuleBackground();
        }
        return color;
    }

    // Outline

    @ModifyArgs(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=outline")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme$ThreeStateColorSetting;<init>(Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)V", ordinal = 0))
    private void outlineColor(Args args) {
        if (args.get(0) instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            IRecolorGuiTheme.TriColorSetting triColorSetting = iRecolorGuiTheme.getOutlineColor();
            args.set(3, triColorSetting.c1());
            args.set(4, triColorSetting.c2());
            args.set(5, triColorSetting.c3());
        }
    }

    // Separator

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=separator-text")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor separatorText(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getSeparatorText();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=separator-center")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor separatorCenter(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getSeparatorCenter();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=separator-edges")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor separatorEdges(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getSeparatorEdges();
        }
        return color;
    }

    // Scrollbar

    @ModifyArgs(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=Scrollbar")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme$ThreeStateColorSetting;<init>(Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)V", ordinal = 0))
    private void scrollbarColor(Args args) {
        if (args.get(0) instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            IRecolorGuiTheme.TriColorSetting triColorSetting = iRecolorGuiTheme.getScrollbarColor();
            args.set(3, triColorSetting.c1());
            args.set(4, triColorSetting.c2());
            args.set(5, triColorSetting.c3());
        }
    }

    // Slider

    @ModifyArgs(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=slider-handle")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme$ThreeStateColorSetting;<init>(Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)V", ordinal = 0))
    private void sliderHandle(Args args) {
        if (args.get(0) instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            IRecolorGuiTheme.TriColorSetting triColorSetting = iRecolorGuiTheme.getSliderHandle();
            args.set(3, triColorSetting.c1());
            args.set(4, triColorSetting.c2());
            args.set(5, triColorSetting.c3());
        }
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=slider-left")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor sliderLeft(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getSliderLeft();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=slider-right")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor sliderRight(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getSliderRight();
        }
        return color;
    }

    // Starscript

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=starscript-text")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor starscriptText(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getStarscriptText();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=starscript-braces")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor starscriptBraces(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getStarscriptBraces();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=starscript-parenthesis")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor starscriptParenthesis(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getStarscriptParenthesis();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=starscript-dots")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor starscriptDots(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getStarscriptDots();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=starscript-commas")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor starscriptCommas(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getStarscriptCommas();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=starscript-operators")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor starscriptOperators(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getStarscriptOperators();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=starscript-strings")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor starscriptStrings(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getStarscriptStrings();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=starscript-numbers")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor starscriptNumbers(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getStarscriptNumbers();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=starscript-keywords")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor starscriptKeywords(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getStarscriptKeywords();
        }
        return color;
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=starscript-accessed-objects")), at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/themes/meteor/MeteorGuiTheme;color(Lmeteordevelopment/meteorclient/settings/SettingGroup;Ljava/lang/String;Ljava/lang/String;Lmeteordevelopment/meteorclient/utils/render/color/SettingColor;)Lmeteordevelopment/meteorclient/settings/Setting;", ordinal = 0), index = 3)
    private SettingColor starscriptAccessedObjects(SettingColor color) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            return iRecolorGuiTheme.getStarscriptAccessedObjects();
        }
        return color;
    }
}
