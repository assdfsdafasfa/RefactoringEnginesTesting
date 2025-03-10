package refactoring.handlers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;

import refactoring.Info.RenameElemInfo;
import refactoring.Info.RenameMethodInfo;
import refactoring.random.select.PerformRefactoring;

public class SampleHandler extends AbstractHandler {
	public static long start;
	public static int end;
	public static final boolean DESCRIPTOR_TEST = false;
	public boolean fIsPreDeltaTest = false;
	static List<String> projectList = new ArrayList<>();
	static List<RenameElemInfo> parameterInfos = new ArrayList<>();
	static List<RenameElemInfo> localVariableInfos = new ArrayList<>();
	static List<RenameElemInfo> fieldInfos = new ArrayList<>();
	static List<RenameMethodInfo> methodInfos = new ArrayList<>();
	static List<RenameElemInfo> classInfos = new ArrayList<>();
	static List<IFile> javaFiles = new ArrayList<>();
	protected static final String TEST_OUTPUT_INFIX = "D:\\eclipse\\workspace\\Dataset\\";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
//		getICompilationUnit();
//		testTypeRenameRefactoring();
//		ExtractVariableHandler.handlerExtractVariable(event);
//		testMethodRenameRefactoring(event);
//		testFieldRenameRefactoring();
//		testLocalVariableRenameRefactoring();
//		testParameterRenameRefactoring();
//		System.out.println("111");
		try {
//			RandomSelectExpression.handlerExtractVariable(event);
			PerformRefactoring.performRefactoring(event);
		} catch (CoreException | OperationCanceledException | InvocationTargetException | InterruptedException
				| IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
