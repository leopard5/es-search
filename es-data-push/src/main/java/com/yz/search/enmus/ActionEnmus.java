package com.yz.search.enmus;

public enum ActionEnmus {
    /**
     *
     */
    ONLINE((byte) 1),
    /**
     *
     */
    OFFLINE((byte) 2),
    ;

    private byte value;

    ActionEnmus(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public static boolean exists(Byte status) {
        if (status == null) {
            return false;
        }
        byte s = status.byteValue();
        return exists(s);
    }

    public static boolean exists(byte s) {
        for (ActionEnmus element : ActionEnmus.values()) {
            if (element.value == s) {
                return true;
            }
        }
        return false;
    }

    public boolean equal(Byte val) {
        return val == null ? false : val.byteValue() == this.value;
    }
}
