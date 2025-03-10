package Controller;

import Model.ExtractMethodInfo;
import com.intellij.codeInsight.CodeInsightUtil;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.refactoring.extractMethod.ExtractMethodHandler;
import com.intellij.refactoring.extractMethod.ExtractMethodProcessor;
import com.intellij.refactoring.extractMethod.PrepareFailedException;
import com.intellij.refactoring.util.duplicates.Match;
import com.intellij.util.IncorrectOperationException;

import java.util.List;

public class ExtractMethod {

    public ExtractMethodInfo extractMethodInfo;
    public ExtractMethod(ExtractMethodInfo extractMethodInfo){
        this.extractMethodInfo = extractMethodInfo;
    }
    public static boolean performExtractMethod(Editor editor,
                                               PsiFile file,
                                               Project project,
                                               int startOffset,
                                               int endOffset
//                                               String newNameOfFirstParam
//                                             boolean doRefactor,
//                                             boolean replaceAllDuplicates,
//                                             final boolean extractChainedConstructor,
//                                             int... disabledParams
    )
            throws PrepareFailedException, IncorrectOperationException {

//        int startOffset = editor.getSelectionModel().getSelectionStart();
//        int endOffset = editor.getSelectionModel().getSelectionEnd();

        PsiElement[] elements;
        PsiExpression expr = CodeInsightUtil.findExpressionInRange(file, startOffset, endOffset);
        if (expr != null) {
            elements = new PsiElement[]{expr};
        }
        else {
            elements = CodeInsightUtil.findStatementsInRange(file, startOffset, endOffset);
        }
        if (elements.length == 0) {
            PsiElement psiElement = file.findElementAt(startOffset);
            if (psiElement instanceof PsiExpression) {
                PsiExpression expression = (PsiExpression) psiElement;
                if (expression != null) {
                    elements = new PsiElement[]{expression};
                }
            }
        }

        final ExtractMethodProcessor processor =
                new ExtractMethodProcessor(project, editor, elements, null, "Extract Method", "newMethod", null);
        processor.setShowErrorDialogs(false);
        processor.setChainedConstructor(false);

        if (!processor.prepare()) {
            return false;
        }

        if (true) {
//            if (disabledParams != null) {
//                for (int param : disabledParams) {
//                    processor.doNotPassParameter(param);
//                }
//            }
//            if (newNameOfFirstParam != null) {
//                processor.changeParamName(0, newNameOfFirstParam);
//            }
            ExtractMethodHandler.extractMethod(project,processor);// test
        }

        if (true) {
            final Boolean hasDuplicates = processor.hasDuplicates();
            if (hasDuplicates == null || hasDuplicates.booleanValue()) {
                final List<Match> duplicates = processor.getDuplicates();
                for (final Match match : duplicates) {
                    if (!match.getMatchStart().isValid() || !match.getMatchEnd().isValid()) continue;
                    PsiDocumentManager.getInstance(project).commitAllDocuments();
                    processor.processMatch(match);
                }
            }
        }

        return true;
    }
}
