import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

final class PluginToolWindowFactory implements ToolWindowFactory, DumbAware {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        PluginState state = ApplicationManager.getApplication().getService(PluginState.class);
        Content content = ContentFactory.getInstance().createContent(new GeneratorForm(state).getMainPanel(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

}