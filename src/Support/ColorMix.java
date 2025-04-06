package Support;

import java.awt.*;

public class ColorMix {
    /* values */

    public Color fill, border, middlePoint;

    /* static values / color combinations */

    public static ColorMix BLACK_RIM = new ColorMix(null, Color.BLACK, Color.BLACK);
    public static ColorMix BASIC_COLOR_MIX = new ColorMix(Color.WHITE, Color.BLACK);

    /* constructor */

    //setting by two colors
    public ColorMix(Color fill, Color border) {
        this(fill, border, null);
    }

    //setting by three colors
    public ColorMix(Color fill, Color border, Color middlePoint) {
        this.fill = fill;
        this.border = border;
        this.middlePoint = middlePoint;
    }

    /* basic methods */
    public String toString() {
        return "[fill:" + fill + ";border:" + border + "]";
    }
}
