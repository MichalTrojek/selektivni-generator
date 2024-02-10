import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "PluginState",
        storages = {@Storage("PluginState.xml")}
)
public class PluginState implements PersistentStateComponent<PluginState> {

    private String apiNames = "";
    private String modelNames =  "";
    private String result = "";
    private String webClientPath = "";

    @Nullable
    @Override
    public PluginState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull PluginState state) {
        this.apiNames = state.apiNames;
        this.modelNames = state.modelNames;
        this.webClientPath = state.webClientPath;
    }

    public String getApiNames() {
        return apiNames;
    }

    public void setApiNames(String apiNames) {
        this.apiNames = apiNames;
    }

    public String getModelNames() {
        return modelNames;
    }

    public void setModelNames(String modelNames) {
        this.modelNames = modelNames;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getWebClientPath() {
        return webClientPath;
    }

    public void setWebClientPath(String webClientPath) {
        this.webClientPath = webClientPath;
    }
}
