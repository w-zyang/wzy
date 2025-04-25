package test_treemap_findwords;

import java.io.*;
import java.util.*;

/**
 * @author wzy
 * @version 2.0
 */
public class FileOperation {
    private static final int MAX_MATCH_LENGTH = 4;
    private String[] filePaths;
    private BufferedReader reader;
    private TreeMap<String, MyTreeSet<String>> treeMap;
    private Set<String> dictionary;
    private Set<String> readFiles;
    private String outputPath = "D:\\java\\TreeMapFindWords\\resource\\output.txt";
    private BigramLanguageModel bigramModel;
    class MyTreeSet<E> extends TreeSet<E> {
        @Override
        public String toString() {
            Iterator<E> it = iterator();
            if (!it.hasNext())
                return "[]";

            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (;;) {
                E e = it.next();
                sb.append(e == this ? "(this Collection)" : e);
                if (!it.hasNext())
                    return sb.append("]").toString();
                sb.append(", ");
            }
        }
    }
    public FileOperation() {
        LoadReadFiles();                             // 加载已读文件列表
        LoadDictionary();                            // 加载中文词典
        GetFilePath();                               // 获取当前新文件路径
        if (filePaths.length == 0) return;           // 如果没有新文件，直接结束

        bigramModel = new BigramLanguageModel();
        List<String> allWords = new ArrayList<>();

        CreateTreeMap(allWords);                     // 读取新文件并构建 TreeMap
        bigramModel.train(allWords);

        WriteFile(outputPath);
    }

    private void LoadReadFiles() {
        readFiles = new HashSet<>();
        File file = new File("D:\\java\\TreeMapFindWords\\resource\\read_files.txt");
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    readFiles.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void LoadDictionary() {
        dictionary = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader("D:\\java\\TreeMapFindWords\\resource\\Dictionary.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                dictionary.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void GetFilePath() {
        File folder = new File("D:\\java\\TreeMapFindWords\\documents");
        if (folder.exists() && folder.isDirectory()) {
            filePaths = folder.list((dir, name) -> {
                File f = new File(dir, name);
                return f.isFile() &&!readFiles.contains(f.getAbsolutePath());
            });
            for (int i = 0; i < filePaths.length; i++) {
                filePaths[i] = new File(folder, filePaths[i]).getAbsolutePath();
            }
        } else {
            filePaths = new String[0];
        }
    }

    private void CreateTreeMap(List<String> allWords) {
        treeMap = new TreeMap<String, MyTreeSet<String>>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        for (int i = 0; i < filePaths.length; i++) {
            ReadFile(filePaths[i], allWords);
        }
    }

    private void ReadFile(String filePath, List<String> allWords) {
        File file = new File(filePath);
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (isChinese(line)) {
                    processChinese01(line, filePath, allWords);
                } else {
                    processEnglish(line, filePath, allWords);
                }
            }
            reader.close();
            SaveReadFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isChinese(String line) {
        for (char c : line.toCharArray()) {
            if (Character.UnicodeScript.of(c) == Character.UnicodeScript.HAN) {
                return true;
            }
        }
        return false;
    }

    private void processEnglish(String line, String filePath, List<String> allWords) {
        String[] words = line.split("[ ,.;]");
        for (String word : words) {
            word = word.toLowerCase().trim();
            if (word.isEmpty()) continue;
            treeMap.computeIfAbsent(word, k -> new MyTreeSet<>()).add(filePath);
            allWords.add(word);
        }
    }

    private void processChinese01(String line, String filePath, List<String> allWords) {
        int start = 0;
        while (start < line.length()) {
            boolean found = false;
            for (int len = Math.min(MAX_MATCH_LENGTH, line.length() - start); len > 0; len--) {
                String word = line.substring(start, start + len);
                if (dictionary.contains(word)) {
                    treeMap.computeIfAbsent(word, k -> new MyTreeSet<>()).add(filePath);
                    allWords.add(word);
                    start += len;
                    found = true;
                    break;
                }
            }
            if (!found) {
                // 如果没有匹配到，按单字处理
                String singleChar = line.substring(start, start + 1);
                treeMap.computeIfAbsent(singleChar, k -> new MyTreeSet<>()).add(filePath);
                allWords.add(singleChar);
                start++;
            }
        }
    }

    private void SaveReadFile(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\java\\TreeMapFindWords\\resource\\read_files.txt", true))) {
            bw.write(filePath);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void WriteFile(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, MyTreeSet<String>> entry : treeMap.entrySet()) {
                StringBuilder sb = new StringBuilder(entry.getKey());
                sb.append("=");
                for (String file : entry.getValue()) {
                    sb.append(file).append(",");
                }
                if (sb.charAt(sb.length() - 1) == ',') {
                    sb.deleteCharAt(sb.length() - 1);
                }
                bw.write(sb.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 提供获取 Bigram 模型的方法
    public BigramLanguageModel getBigramModel() {
        return bigramModel;
    }
}