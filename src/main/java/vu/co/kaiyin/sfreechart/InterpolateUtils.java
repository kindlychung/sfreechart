package vu.co.kaiyin.sfreechart;

import java.awt.*;

/**
 * Created by kaiyin on 2/11/16.
 */
public class InterpolateUtils {

    public static int intInterpolate(int i1, int i2, double ratio) {
        int diff = (int) ((i2 - i1) * ratio);
        return i1 + diff;
    }

    public static Color colorInterpolate(Color c1, Color c2, double ratio) {
        int red = intInterpolate(c1.getRed(), c2.getRed(), ratio);
        int green = intInterpolate(c1.getGreen(), c2.getGreen(), ratio);
        int blue = intInterpolate(c1.getBlue(), c2.getBlue(), ratio);
        return new Color(red, green, blue);
    }
}
