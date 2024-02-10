import java.util.ResourceBundle;

public class PluginBundle {

    private static final String BUNDLE_NAME = "plugin";

    public static String getString(String key) {
        return ResourceBundle.getBundle(BUNDLE_NAME).getString(key);
    }

}
