package refactoring.undo;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.DefaultOperationHistory;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.undo.DocumentUndoManagerRegistry;
import org.eclipse.text.undo.IDocumentUndoManager;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.operations.UndoActionHandler;

public class UndoHistory {
	/// undo refactoring
	public static void undoRefactoringHistory(IFile file) {
		try {
			TextFileDocumentProvider documentProvider = new TextFileDocumentProvider();
			documentProvider.connect(file);
			IDocument document = documentProvider.getDocument(file);
			// Undo the last change in the document undo manager's history
			IDocumentUndoManager undoManager = DocumentUndoManagerRegistry.getDocumentUndoManager(document);
			if (undoManager != null) {
				undoManager.undo();
				System.out.println("Undo operation on refactoring history successful.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void undo() {

		IUndoContext context = (IUndoContext) ResourcesPlugin.getWorkspace().getAdapter(IUndoContext.class);

		IWorkbenchPartSite workbenchpartSite = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActivePart().getSite();

		IOperationHistory operationHistory = PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
		UndoActionHandler undoAction = new UndoActionHandler(workbenchpartSite, context);
		DefaultOperationHistory defaultOperationHistory = (DefaultOperationHistory) operationHistory;

		IUndoableOperation[] operationss = operationHistory.getUndoHistory(IOperationHistory.GLOBAL_UNDO_CONTEXT);

		if (operationss.length > 0)
			if (operationss[operationss.length - 1].canUndo()) {
				try {
					operationHistory.undoOperation(operationss[operationss.length - 1], null, undoAction);
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
}
