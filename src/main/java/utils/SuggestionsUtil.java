package utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.ArrayList;
import java.util.Collection;

public class SuggestionsUtil {

    public static Collection<String> getSuggestions(Project project) {
        Collection<String> suggestions = new ArrayList<>();
        Collection<VirtualFile> virtualFiles = new ArrayList<>();
        for (Project openedProject : ProjectManager.getInstance().getOpenProjects()) {
            virtualFiles.addAll(FilenameIndex.getAllFilesByExt(project, "java", GlobalSearchScope.projectScope(openedProject)));
        }
        for (VirtualFile virtualFile : virtualFiles) {
            PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
            if (psiFile != null) {
                suggestions.add(psiFile.getName().replace(".java", ""));
            }
        }
        return suggestions;
    }
}
