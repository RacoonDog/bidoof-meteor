package io.github.racoondog.bidoofmeteor.mixininterface;

import meteordevelopment.meteorclient.utils.render.color.Color;

public interface IRenderer2D {
    void texQuadHFlip(double x, double y, double width, double height, Color color);
    void texQuadVFlip(double x, double y, double width, double height, Color color);
    void texQuadHVFlip(double x, double y, double width, double height, Color color);
}
