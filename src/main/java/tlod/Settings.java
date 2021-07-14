package tlod;

public class Settings {

    public static boolean isToggled() {
        return toggled;
    }

    public static void setToggled(boolean toggled) {
        Settings.toggled = toggled;
    }

    public static int getKey() {
        return key;
    }

    public static void setKey(int key) {
        Settings.key = key;
    }

    private static boolean toggled = false;

    private static int key = 0;

    public static void toggle() {
        if (toggled) {
            toggled = false;
            DerpMod.derpBot.onDisable();
        } else {
            toggled = true;
            DerpMod.derpBot.onEnable();
        }
    }
}