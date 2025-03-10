package refactoring.handlers;

public class PreWorkHandler {

//	public PreWorkHandler(IJavaProject javaProject,Exp1Record exp1Record) {
//        String projectPath=Constants.EXP1_PROJECT_ROOT+javaProject.getPath().toString();
//         
//        Git git=null;
//        try {
//        	git=Git.open(new File(projectPath));
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
//        Repository repository = git.getRepository();
//        MyLog.add("commit id:"+exp1Record.getCommitId());
//        try {
//        	git.checkout().setForced(true).setName(exp1Record.getCommitId()).call();
//		} catch (CheckoutConflictException e) {
//			e.printStackTrace();
//		} catch (GitAPIException e) {
//			e.printStackTrace();
//		}
//        try {
//        	Iterable<RevCommit> iterable = git.log().call(); 
//			Iterator<RevCommit> iterator = iterable.iterator();
//			MyLog.add("switch to "+iterator.next().getName()+" !");
//		} catch (NoWorkTreeException e) {
//			e.printStackTrace();
//		} catch (GitAPIException e) {
//			e.printStackTrace();
//		} 
//        repository.close();
//        git.close();
//	}

}
