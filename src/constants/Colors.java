package constants;

import java.awt.Color;

public enum Colors {
    CREAMY_LATTE(243, 233, 215),
    CARAMEL_ROAST(176, 137, 104),
    MOCHA_BEAN(122, 85, 58),
    WARM_CAPP(59, 42, 34),
    ESPRESSO_SHOT(214, 191, 166);

    private final Color color;

    Colors(int r, int g, int b) {
        this.color = new Color(r, g, b);
    }

    public Color getColor() {
        return color;
    }
}
