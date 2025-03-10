package refactoring.random.select;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SelectRefactoring {
	public static int selectRefNumbers() {
		Random random = new Random();
		int num = random.nextInt(Constant.MAX_REFACTORING_NUMBERS);
		return num;
	}

	public static List<Integer> ramdomRefactoringType(int n) {
		List<Integer> refList = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			int num = random.nextInt(Constant.MAX_REFACTORING_TYPES);
			refList.add(num);
		}
		return refList;
	}

	public static int randomRenameType() {
		Random random = new Random();
		return random.nextInt(Constant.MAX_RENAME_TYPES);
	}

	public static int randomRefactoringIdentifier(int n) {
		Random random = new Random();
		return random.nextInt(n);
	}

	public static int randomStatementEnd(int start, int end) {
		Random random = new Random();
		return random.nextInt(end - start + 1) + start;
	}

	public static int randomInlineMethod(int n, List<Integer> excludes) {
		Random random = new Random();
		int num = n + 1;
		if (excludes.size() > 0) {
			while (!excludes.contains(num)) {
				num = random.nextInt(n);
			}
			return num;
		} else {
			return random.nextInt(n);
		}
	}

	public static int selectRefactoringNumber() {
		Random random = new Random();
		int num = random.nextInt(10);
		return num;
//		if (refactoringTypeNumber <= 1) {
//			return 0;
//		}
//		if (refactoringTypeNumber >= 2 && refactoringTypeNumber <= 9) {
//			return 1;
//		} else {
//			int numbers = refactoringTypeNumber / 10;
//			return numbers;
//		}
	}
}
