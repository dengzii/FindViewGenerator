import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;

public class GenerateFindViewAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        Editor editor = e.getData(PlatformDataKeys.EDITOR);


    }
}
