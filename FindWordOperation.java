package test_treemap_findwords;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author wzy
 * @version 1.0
 */
public class FindWordOperation {
    String[] destword;
    BufferedReader reader;
    private BigramLanguageModel bigramModel;
    private Scanner scanner; // 新增一个 Scanner 对象

    public FindWordOperation(BigramLanguageModel bigramModel) {
        this.bigramModel = bigramModel;
        this.scanner = new Scanner(System.in); // 在构造函数中初始化 Scanner 对象
        InitDestWords();
        FindWords("D:\\java\\TreeMapFindWords\\resource\\output.txt");
        provideSuggestions();
        scanner.close(); // 在程序结束时关闭 Scanner 对象
    }

    private void InitDestWords() {
        System.out.println("输入任意多个单词：");
        String str = scanner.nextLine();
        String[] words = str.split("[ ,;.]");
        destword = new String[words.length];
        for (int i = 0; i < words.length; i++) {
            destword[i] = words[i].toLowerCase();
        }
        Arrays.sort(destword);
    }

    private void FindWords(String filePath) {
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            int count = 0;
            while (line != null && count < destword.length) {
                String[] information = line.split("[=,]");
                if (information[0].compareTo(destword[count]) == 0) {
                    System.out.print(information[0] + "出现在");
                    for (int i = 1; i < information.length; i++) {
                        System.out.print(information[i] + " ");
                    }
                    System.out.println();
                    line = reader.readLine();
                    count++;
                } else if (information[0].compareTo(destword[count]) > 0) {
                    System.out.println(destword[count] + "未找到！");
                    count++;
                } else {
                    line = reader.readLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void provideSuggestions() {
        System.out.println("输入查询词以获取关键词建议（输入 'q' 退出）：");
        String query;
        while (true) {
            query = scanner.nextLine().toLowerCase();
            if ("q".equals(query)) {
                break;
            }
            List<String> suggestions = bigramModel.getSuggestions(query, 5);
            if (suggestions.isEmpty()) {
                System.out.println("没有找到相关的关键词建议。");
            } else {
                System.out.println("关键词建议：");
                for (String suggestion : suggestions) {
                    System.out.println(suggestion);
                }
            }
        }
    }
}