package Utils;

import com.intellij.openapi.project.Project;
import org.gradle.internal.impldep.org.eclipse.jgit.api.Git;
import org.gradle.internal.impldep.org.eclipse.jgit.api.ResetCommand;
import org.gradle.internal.impldep.org.eclipse.jgit.api.errors.*;

import java.io.File;
import java.io.IOException;

public class GitHandler {
    public GitHandler(String projetcPath) {
        Git git = null;
        try {
            git = Git.open(new File(projetcPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        restoreCode(git);
    }
    public void restoreCode(Git git) {
        try {
            git.reset().setMode(ResetCommand.ResetType.HARD).call();
        } catch (RefAlreadyExistsException e) {
            e.printStackTrace();
        } catch (RefNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidRefNameException e) {
            e.printStackTrace();
        } catch (CheckoutConflictException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        git.close();
    }
}
