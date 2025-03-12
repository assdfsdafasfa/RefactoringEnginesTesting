package utils;

import java.util.ArrayList;
import java.util.List;

public class Test {
	// n 行 n列 矩阵
	static int n = 4;
	static int[][] matrix = { { 19, 20, 22, 21 }, { 18, 18, 20, 20 }, { 16, 16, 20, 18 }, { 15, 20, 20, 19 } };
	static List<Integer> rowList = new ArrayList<>();
	static List<Integer> colList = new ArrayList<>();
	static List<Integer> sameList = new ArrayList<>();
	static boolean flag = true;
	static boolean flag1 = true;

	public static void main(String[] args) {
		int[][] matrixArray = { { 1, 1, 1, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 1 } };
		// 获取第一种情况可能点
		for (int i = 1; i < n; i++) {
			rowList.add(matrix[0][i]);
			colList.add(matrix[i][0]);
			sameList.add(matrix[i][i]);
		}

		for (int a = 0; a < n; a++) {
			for (int b = 0; b < n; b++) {
				if (a == 0 && b == 0) {
				} else if (a == 0 && b > 1) {

					for (int i = 0; i < b - 1; i++) {
						if (rowList.get(i) > matrix[0][b]) {
							matrixArray[0][b] = 0;
							break;
						}
					}
				} else if (b == 0 && a > 1) {
					for (int i = 0; i < a - 1; i++) {
						if (colList.get(i) > matrix[a][0]) {
							matrixArray[a][0] = 0;
							break;
						}
					}
				} else if (a == b && a != 1) {
					for (int i = 0; i < a - 1; i++) {
						if (sameList.get(i) > matrix[a][b]) {
							matrixArray[a][b] = 0;
							break;
						}
					}
				} else if (a != 0 && a < b) {
					// 横坐标小于纵坐标
					if (rowRecursiveFunction(a, b, matrix[a][b]) == false) {
						matrixArray[a][b] = 0;
					}
					flag = true;
				} else if (b != 0 && a > b) {
					// 横坐标大于纵坐标
					if (colRecursiveFunction(a, b, matrix[a][b]) == false) {
						matrixArray[a][b] = 0;
					}
					flag1 = true;
				}
			}
		}

		for (int i = 0; i < matrixArray.length; i++) { // 遍历每一行
			for (int j = 0; j < matrixArray[i].length; j++) { // 遍历每一列
				System.out.print(matrixArray[i][j] + "\t"); // 打印每个元素
			}
			System.out.println(); // 每一行打印完后换行
		}

	}

	public static int row(int val1, int val2, int a, int b) {
		return (val1 * a + val2 * (b - a)) / b;
	}

	public static int cal(int val1, int val2, int a, int b) {
		return (val1 * b + val2 * (a - b)) / a;
	}

	public static boolean rowRecursiveFunction(int a, int b, int val) {
		if (flag == true) {
			if (row(matrix[a][b - 1], matrix[a - 1][b - 1], a, b) > val) {
				return false;
			}
			if (a - 1 <= 1) {
				return true;
			} else {
				flag = false;
			}
			return rowRecursiveFunction(a - 1, b - 1, val);
		} else {
			if (row(matrix[a][b], matrix[a][b - 1], a, b) > val) {
				return false;
			}
			if (a - 1 <= 1) {
				return true;
			} else {
				flag = true;
			}
			return rowRecursiveFunction(a, b - 1, val);
		}

	}

	public static boolean colRecursiveFunction(int a, int b, int val) {
		if (flag1 == true) {
			if (cal(matrix[a - 1][b], matrix[a - 1][b - 1], a, b) > val) {
				return false;
			}
			if (b - 1 <= 1) {
				return true;
			} else {
				flag1 = false;
			}
			return rowRecursiveFunction(a - 1, b - 1, val);
		} else {
			if (cal(matrix[a][b], matrix[a - 1][b], a, b) > val) {
				return false;
			}
			if (a - 1 <= 1) {
				return true;
			} else {
				flag1 = true;
			}
			return rowRecursiveFunction(a, b - 1, val);
		}
	}

}
