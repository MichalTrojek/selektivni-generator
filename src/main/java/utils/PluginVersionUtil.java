package utils;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;

public class PluginVersionUtil {
    public static String getPluginVersion() {
        // Get the plugin ID
        PluginId pluginId = PluginId.getId("com.github.michaltrojek.selective-generator");

        // Get the plugin descriptor
        IdeaPluginDescriptor pluginDescriptor = PluginManager.getPlugin(pluginId);

        if (pluginDescriptor != null) {
            // Get the plugin version from the descriptor
            return pluginDescriptor.getVersion();
        } else {
            return null;
        }
    }
}