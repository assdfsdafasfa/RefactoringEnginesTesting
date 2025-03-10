package Utils;

import Model.ExpressionInfo;
import Model.InlineMethodInfo;
import Model.RenameMethodInfo;

import java.util.*;

public class ShuffleList {
    public static void shuffleList(List<Integer> list) {
        Random random = new Random();
        for (int i = list.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            swap(list, i, j);
        }
    }

    private static void swap(List<Integer> list, int i, int j) {
        int temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    public static Set<Integer> randomList(List<RenameMethodInfo> allMethodList){
        Random random = new Random();
        Set<Integer> numbers = new HashSet<>();
        int numberOfElements = (int) ((allMethodList.size()-1) * 0.1);
        if(numberOfElements>0){
            while (numbers.size() < numberOfElements) {
                int randomNumber = random.nextInt(0,allMethodList.size()-1); // 生成随机整数
                numbers.add(randomNumber);
            }
        }
        return numbers;
    }
    public static Set<Integer> randomExpressionList(List<ExpressionInfo> allMethodList){
        Random random = new Random();
        Set<Integer> numbers = new HashSet<>();
        int numberOfElements = (int) ((allMethodList.size()-1) * 0.3);
        if(numberOfElements>0){
            while (numbers.size() < numberOfElements) {
                int randomNumber = random.nextInt(0,allMethodList.size()-1); // 生成随机整数
                numbers.add(randomNumber);
            }
        }
        return numbers;
    }

    public static Set<Integer> randomCalledMethodList(List<InlineMethodInfo> allMethodList){
        Random random = new Random();
        Set<Integer> numbers = new HashSet<>();
        int numberOfElements = (int) ((allMethodList.size()-1) * 0.2);
        if(numberOfElements>0){
            while (numbers.size() < numberOfElements) {
                int randomNumber = random.nextInt(0,allMethodList.size()-1);
                numbers.add(randomNumber);
            }
        }
        return numbers;
    }
}
