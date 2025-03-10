package Action;

import java.util.ResourceBundle;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.jface.text.IUndoManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.NotDefinedException;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.IUpdate;
import org.eclipse.ui.texteditor.TextEditorAction;

public class UndoRefactoringAction implements IHandler{

	public UndoRefactoringAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        // 获取 ICommandService
        ICommandService commandService = PlatformUI.getWorkbench().getService(ICommandService.class);
        // 获取撤销所有操作的命令
        Command undoAllCommand = commandService.getCommand(IWorkbenchCommandConstants.EDIT_UNDO);
        undoAllCommand.setEnabled(true);
        // 获取 IHandlerService
        IHandlerService handlerService = PlatformUI.getWorkbench().getService(IHandlerService.class);
        try {
            // 构建参数化的命令
            ParameterizedCommand parameterizedCommand = ParameterizedCommand.generateCommand(undoAllCommand, null);
            // 执行撤销所有操作的命令
            handlerService.executeCommand(parameterizedCommand, null);
        } catch (NotEnabledException | NotHandledException | org.eclipse.core.commands.common.NotDefinedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isEnabled() {
        return true;  // 可根据需要设置是否启用
    }

    @Override
    public boolean isHandled() {
        return true;
    }

	@Override
	public void addHandlerListener(IHandlerListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeHandlerListener(IHandlerListener arg0) {
		// TODO Auto-generated method stub
		
	}
}
