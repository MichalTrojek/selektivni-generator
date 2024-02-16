import com.intellij.openapi.project.Project;
import com.intellij.ui.TextFieldWithAutoCompletion;
import com.intellij.ui.TextFieldWithAutoCompletionListProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;


public class TextFieldWithSuggestions extends TextFieldWithAutoCompletion<String> {
    public TextFieldWithSuggestions(Project project) {
        super(project, new MyAutoCompletionProvider(new ArrayList<>()), true, false, "");
    }

    private static class MyAutoCompletionProvider extends TextFieldWithAutoCompletionListProvider<String> {


        protected MyAutoCompletionProvider(@Nullable Collection<String> variants) {
            super(variants);
        }


        @Override
        protected @NotNull String getLookupString(@NotNull String item) {
            return item;
        }

        @Override
        public boolean isDumbAware() {
            return super.isDumbAware();
        }

        @Override
        public Comparator<String> reversed() {
            return super.reversed();
        }

        @Override
        public Comparator<String> thenComparing(Comparator<? super String> other) {
            return super.thenComparing(other);
        }

        @Override
        public <U> Comparator<String> thenComparing(Function<? super String, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
            return super.thenComparing(keyExtractor, keyComparator);
        }

        @Override
        public <U extends Comparable<? super U>> Comparator<String> thenComparing(Function<? super String, ? extends U> keyExtractor) {
            return super.thenComparing(keyExtractor);
        }

        @Override
        public Comparator<String> thenComparingInt(ToIntFunction<? super String> keyExtractor) {
            return super.thenComparingInt(keyExtractor);
        }

        @Override
        public Comparator<String> thenComparingLong(ToLongFunction<? super String> keyExtractor) {
            return super.thenComparingLong(keyExtractor);
        }

        @Override
        public Comparator<String> thenComparingDouble(ToDoubleFunction<? super String> keyExtractor) {
            return super.thenComparingDouble(keyExtractor);
        }
    }
}