package refactoring.random.type;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.internal.ui.refactoring.RefactoringExecutionHelper;
import org.eclipse.jdt.ui.refactoring.RefactoringSaveHelper;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import refactoring.json.RefactoringRecord;
import refactoring.json.SaveData;
import refactoring.random.gpt.GptApi;
import refactoring.random.model.EclipseExtractConstantRefactoring;
import refactoring.random.select.Constant;
import refactoring.random.select.PerformRefactoring;

public class ExtractConstantRefactoringPerform {
	// An expression must be selected to activate this refactoring.

	public static void performExtractConstantRefactoring(IFile file, CompilationUnit astRoot)
			throws OperationCanceledException, InvocationTargetException, CoreException, InterruptedException {
		Map<VariableDeclarationStatement, Expression> map = extractStringsAndNumbers(astRoot);
		if (map.size() > 0) {
			Map<Type, Expression> shuffledMap = suffledMap(map);
//			int num = SelectRefactoring.selectRefactoringNumber(shuffledMap.size());
			SelectRefactoring(shuffledMap, astRoot);
		} else {
			System.out.println("expression is null, not refactoring");
		}
	}

	private static void SelectRefactoring(Map<Type, Expression> expressions, CompilationUnit astRoot)
			throws OperationCanceledException, CoreException {
		int temp = 0;
		for (Entry<Type, Expression> exp : expressions.entrySet()) {
			Type type = exp.getKey();
			Expression selectedExp = exp.getValue();
			System.out.println("exp:" + selectedExp);
			int start = selectedExp.getStartPosition();
			int length = selectedExp.getLength();
			String newName = GptApi.printMessage(Constant.EXTRACT_CONSTANT_PROMPT + selectedExp);
			newName = newName.replaceAll(Constant.regEx, "");
			EclipseExtractConstantRefactoring refactoring = new EclipseExtractConstantRefactoring(astRoot, type, start,
					length);
			refactoring.setConstantName(newName);
			refactoring.setReplaceAllOccurrences(true);
			RefactoringStatus status = refactoring.checkInitialConditions(new NullProgressMonitor());
//			System.out.println("==========================");
			if (status.isOK()) {
//				System.out.println("------");
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

				RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
						RefactoringCore.getConditionCheckingFailedSeverity(), RefactoringSaveHelper.SAVE_NOTHING, shell,
						PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				try {
					helper.perform(false, false);

					PerformRefactoring.no += 1;
					RefactoringRecord record = new RefactoringRecord(PerformRefactoring.projectNa,
							PerformRefactoring.no, null, newName, PerformRefactoring.filePath, start, length,
							PerformRefactoring.className, "extract constant");
					SaveData saveData = new SaveData(record);
					break;
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

//		while (expressions.size() > 0) {
//			if (expressions.size() == 1) {
//				Expression fDeclaration = expressions.get(0);
//				int start = fDeclaration.getStartPosition();
//				int length = fDeclaration.getLength();
//				EclipseExtractConstantRefactoring refactoring = new EclipseExtractConstantRefactoring(astRoot, start,
//						length);
//				refactoring.setConstantName("newName");
//				refactoring.setReplaceAllOccurrences(true);
//				RefactoringStatus status = refactoring.checkInitialConditions(new NullProgressMonitor());
//				if (status.isOK()) {
//					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
//
//					RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
//							RefactoringCore.getConditionCheckingFailedSeverity(), RefactoringSaveHelper.SAVE_NOTHING,
//							shell, PlatformUI.getWorkbench().getActiveWorkbenchWindow());
//					try {
//						helper.perform(false, false);
//					} catch (InvocationTargetException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				break;
//			} else {
//				int pos = SelectRefactoring.randomRefactoringIdentifier(expressions.size());
//				Expression fDeclaration = expressions.get(pos);
////				System.out.println("fd:" + fDeclaration);
//				int start = fDeclaration.getStartPosition();
//				int length = fDeclaration.getLength();
////				System.out.println("st:" + start + " " + "length:" + length);
//				EclipseExtractConstantRefactoring refactoring = new EclipseExtractConstantRefactoring(astRoot, start,
//						length);
////				String newName = GptApi.printMessage(Constant.EXTRACT_VARIABLE_PROMPT + fDeclaration);
////				System.out.println("newName:" + newName);
//				refactoring.setConstantName("newName");
//				refactoring.setReplaceAllOccurrences(true);
//				RefactoringStatus status = refactoring.checkInitialConditions(new NullProgressMonitor());
////				System.out.println("status:" + status);
//				try {
//					if (status.isOK()) {
//						Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
//
//						RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
//								RefactoringCore.getConditionCheckingFailedSeverity(),
//								RefactoringSaveHelper.SAVE_NOTHING, shell,
//								PlatformUI.getWorkbench().getActiveWorkbenchWindow());
//						helper.perform(false, false);
//						break;
//					} else {
//						expressions.remove(pos);
//						SelectRefactoring(expressions, astRoot);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}

	public static Map<VariableDeclarationStatement, Expression> extractStringsAndNumbers(CompilationUnit astRoot) {
		Map<VariableDeclarationStatement, Expression> map = new HashMap();

		astRoot.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(MethodDeclaration node) {
				MethodDeclaration methodDeclaration = (MethodDeclaration) node;
				Block block = methodDeclaration.getBody();
				if (block != null) {
					block.accept(new ASTVisitor() {

						public boolean visit(VariableDeclarationStatement node) {
//						Type type = node.getType();
							node.accept(new ASTVisitor() {

								public boolean visit(StringLiteral nodes) {
									System.out.println("nodes:" + nodes);
									map.put(node, nodes);
									return true;
								}

								public boolean visit(NumberLiteral nodes) {
									System.out.println("nodes:" + nodes);
									map.put(node, nodes);
									return true;
								}

							});
							return true;
						}
					});
				}
//				block.accept(new ASTVisitor() {
//					public boolean visit(StringLiteral node) {
//						Expression expression = null;
//						expressions.add((Expression) node);
//						return true;
//					}
//					public boolean visit(NumberLiteral node) {
//						expressions.add((Expression) node);
//						return true;
//					}
//				});
				return true;
			}
		});
		return map;
	}

	public static Map<Type, Expression> suffledMap(Map<VariableDeclarationStatement, Expression> map) {
		List<VariableDeclarationStatement> keyList = new ArrayList<>(map.keySet());
		Collections.shuffle(keyList);
		Map<Type, Expression> shuffledMap = new LinkedHashMap<>();
		for (VariableDeclarationStatement key : keyList) {
			shuffledMap.put(key.getType(), map.get(key));
		}
		return shuffledMap;
	}
}
