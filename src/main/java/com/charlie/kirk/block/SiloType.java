package com.charlie.kirk.block;

import net.minecraft.util.StringRepresentable;

public enum SiloType implements StringRepresentable {
    SINGLE("single"),
    TOP("top"),
    MIDDLE("middle"),
    BOTTOM("bottom");

    private final String name;

    private SiloType(String name) {
        this.name = name;
    }

    public String getSerializedName() {
        return this.name;
    }

    public SiloType getOpposite() {
        SiloType var10000;
        switch (this.ordinal()) {
            case 0 -> var10000 = SINGLE;
            case 1 -> var10000 = TOP;
            case 2 -> var10000 = MIDDLE;
            case 3 -> var10000 = BOTTOM;
            default -> throw new MatchException((String)null, (Throwable)null);
        }

        return var10000;
    }
}
