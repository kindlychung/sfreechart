package vu.co.kaiyin.sfreechart;


import org.jfree.chart.HashUtilities;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.util.PublicCloneable;

import java.awt.*;
import java.io.Serializable;

public class ColorPaintScale
        implements PaintScale, PublicCloneable, Serializable {

    /**
     * The lower bound.
     */
    private double lowerBound;

    /**
     * The upper bound.
     */
    private double upperBound;

    private Color startColor;
    private Color endColor;

    /**
     * Creates a new <code>GrayPaintScale</code> instance with default values.
     */
    public ColorPaintScale () {
        this(0.0, 1.0);
    }

    /**
     * Creates a new paint scale for values in the specified range.
     *
     * @param lowerBound the lower bound.
     * @param upperBound the upper bound.
     * @throws IllegalArgumentException if <code>lowerBound</code> is not
     *                                  less than <code>upperBound</code>.
     */
    public ColorPaintScale(double lowerBound, double upperBound) {
        this(lowerBound, upperBound, Color.blue, Color.red);
    }

    public ColorPaintScale(double lowerBound, double upperBound, Color startColor, Color endColor) {
        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException(
                    "Requires lowerBound < upperBound.");
        }
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.startColor = startColor;
        this.endColor = endColor;
    }

    /**
     * Returns the lower bound.
     *
     * @return The lower bound.
     * @see #getUpperBound()
     */
    @Override
    public double getLowerBound() {
        return this.lowerBound;
    }

    /**
     * Returns the upper bound.
     *
     * @return The upper bound.
     * @see #getLowerBound()
     */
    @Override
    public double getUpperBound() {
        return this.upperBound;
    }


    /**
     * Returns a paint for the specified value.
     *
     * @param value the value (must be within the range specified by the
     *              lower and upper bounds for the scale).
     * @return A paint for the specified value.
     */
    @Override
    public Paint getPaint(double value) {
        double v = Math.max(value, this.lowerBound);
        v = Math.min(v, this.upperBound);
        double ratio = (v - this.lowerBound) / (this.upperBound - this.lowerBound);
        return InterpolateUtils.colorInterpolate((Color)startColor, endColor, ratio);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ColorPaintScale)) {
            return false;
        }
        ColorPaintScale that = (ColorPaintScale) obj;
        if (this.lowerBound != that.lowerBound) {
            return false;
        }
        if (this.upperBound != that.upperBound) {
            return false;
        }
        if (this.startColor != that.startColor) {
            return false;
        }
        if(this.endColor != that.endColor) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code for this instance.
     *
     * @return A hash code.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = HashUtilities.hashCode(hash, this.lowerBound);
        hash = HashUtilities.hashCode(hash, this.upperBound);
        hash = 43 * hash + this.startColor.hashCode() * 7 + this.endColor.hashCode() * 2;
        return hash;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}

