package test_treemap_findwords;

import java.util.*;

public class BigramLanguageModel {
    private Map<String, Map<String, Integer>> bigramCounts;
    private Map<String, Integer> unigramCounts;

    public BigramLanguageModel() {
        bigramCounts = new HashMap<>();
        unigramCounts = new HashMap<>();
    }

    // 训练 Bigram 语言模型
    public void train(List<String> words) {
        for (int i = 0; i < words.size() - 1; i++) {
            String w1 = words.get(i);
            String w2 = words.get(i + 1);

            // 更新 unigram 计数
            unigramCounts.put(w1, unigramCounts.getOrDefault(w1, 0) + 1);

            // 更新 bigram 计数
            bigramCounts.computeIfAbsent(w1, k -> new HashMap<>());
            Map<String, Integer> followingWords = bigramCounts.get(w1);
            followingWords.put(w2, followingWords.getOrDefault(w2, 0) + 1);
        }

        // 处理最后一个词的 unigram 计数
        if (!words.isEmpty()) {
            String lastWord = words.get(words.size() - 1);
            unigramCounts.put(lastWord, unigramCounts.getOrDefault(lastWord, 0) + 1);
        }
    }

    // 根据输入的查询词给出关键词建议
    public List<String> getSuggestions(String query, int numSuggestions) {
        List<String> suggestions = new ArrayList<>();
        Map<String, Integer> followingWords = bigramCounts.get(query);
        if (followingWords != null) {
            List<Map.Entry<String, Integer>> entries = new ArrayList<>(followingWords.entrySet());
            entries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

            for (int i = 0; i < Math.min(numSuggestions, entries.size()); i++) {
                suggestions.add(entries.get(i).getKey());
            }
        }
        return suggestions;
    }
}