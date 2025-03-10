package refactoring.random.type;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.internal.ui.refactoring.RefactoringExecutionHelper;
import org.eclipse.jdt.ui.refactoring.RefactoringSaveHelper;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import refactoring.json.RefactoringRecord;
import refactoring.json.SaveData;
import refactoring.random.model.EclipseInlineConstantRefactoring;
import refactoring.random.select.ParseJavaFile;
import refactoring.random.select.PerformRefactoring;

public class InlineConstantRefactoringPerform {
	public static void performInlineConstantRefactoring(IFile file, CompilationUnit astRoot)
			throws OperationCanceledException, InvocationTargetException, CoreException, InterruptedException {

		List<SimpleName> declarations = ParseJavaFile.getFieldDeclaration(astRoot);
		IJavaElement element = JavaCore.create(file);
		ICompilationUnit iCompilationUnit = (ICompilationUnit) element;

		if (declarations.size() > 0) {
			List<IField> fieldList = getFields(iCompilationUnit);
//			Collections.shuffle(declarations);
//			int num = SelectRefactoring.selectRefactoringNumber(declarations.size());

			SelectRefactoring(declarations, iCompilationUnit, astRoot, fieldList);
		}

	}

	private static void SelectRefactoring(List<SimpleName> declarationList, ICompilationUnit iUnit,
			CompilationUnit astRoot, List<IField> fieldList)
			throws OperationCanceledException, CoreException, InvocationTargetException, InterruptedException {
		// TODO Auto-generated method stub
//		while (declarations.size() > 0) {
//			if (declarations.size() == 1) {
//				SimpleName fDeclaration = declarations.get(0);
		for (int a = 0; a < declarationList.size(); a++) {
			SimpleName declarations = declarationList.get(a);
			int start = declarations.getStartPosition();
			int length = declarations.getLength();
			IField iField = getIField(fieldList, declarations.toString());
			if (iField != null) {
				System.out.println("ifeild:" + iField);
				EclipseInlineConstantRefactoring refactoring = new EclipseInlineConstantRefactoring(iUnit, astRoot,
						iField, declarations, start, length);
				RefactoringStatus status = refactoring.checkInitialConditions(new NullProgressMonitor());
				if (status.isOK()) {

					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
					System.out.println("status：" + status);
					RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
							RefactoringCore.getConditionCheckingFailedSeverity(), RefactoringSaveHelper.SAVE_NOTHING,
							shell, PlatformUI.getWorkbench().getActiveWorkbenchWindow());
					helper.perform(false, false);

					PerformRefactoring.no += 1;
					RefactoringRecord record = new RefactoringRecord(PerformRefactoring.projectNa,
							PerformRefactoring.no, declarations.toString(), declarations.toString(),
							PerformRefactoring.filePath, start, length, PerformRefactoring.className,
							"inline constant");
					SaveData saveData = new SaveData(record);
					break;
				}
			}
		}
//				break;
//			} else {
//				int pos = SelectRefactoring.randomRefactoringIdentifier(declarations.size());
//				SimpleName fDeclaration = declarations.get(pos);
//				int start = fDeclaration.getStartPosition();
//				int length = fDeclaration.getLength();
//				IField iField = getIField(fieldList, fDeclaration.toString());
//				EclipseInlineConstantRefactoring refactoring = new EclipseInlineConstantRefactoring(iUnit, astRoot,
//						iField, fDeclaration, start, length);
//				RefactoringStatus status = refactoring.checkInitialConditions(new NullProgressMonitor());
//				if (status.isOK()) {
//					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
//					System.out.println("status：" + status);
//					RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
//							RefactoringCore.getConditionCheckingFailedSeverity(), RefactoringSaveHelper.SAVE_NOTHING,
//							shell, PlatformUI.getWorkbench().getActiveWorkbenchWindow());
//					helper.perform(false, false);
//					break;
//				} else {
//					declarations.remove(pos);
//					SelectRefactoring(declarations, iUnit, astRoot, fieldList);
//				}
//			}

	}

//	}

	public static List<IField> getFields(ICompilationUnit iUnit) throws JavaModelException {
		List<IField> fields = new ArrayList<>();
		IType[] allTypes = iUnit.getAllTypes();
//		for (IType type : allTypes) {type.getFields()
		for (IField field : allTypes[0].getFields()) {
			if (Modifier.isStatic(field.getElementType()) && Modifier.isFinal(field.getElementType())) {
				fields.add(field);
			}
		}
		return fields;
	}

	public static IField getIField(List<IField> iFields, String name) {
		for (IField ifd : iFields) {
			if (ifd.getElementName().endsWith(name)) {
				return ifd;
			}
		}
		return null;
	}
}
