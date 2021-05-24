package cc.sukazyo.sericons.api.energy;

public enum EnumModeProperty {
    BLOCK("block"),
    INPUT("in"),
    OUTPUT("out");

    final String unique;

    EnumModeProperty(String unique) {
        this.unique = unique;
    }

    public String getUnique() {
        return unique;
    }

    public String getName() {
        return this.toString();
    }

    public static EnumModeProperty next(EnumModeProperty curr) {
        return curr == INPUT ? OUTPUT : curr == OUTPUT ? BLOCK : INPUT;
    }
}
