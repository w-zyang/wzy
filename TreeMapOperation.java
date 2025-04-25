package test_treemap_findwords;

public class TreeMapOperation {
    public static void main(String[] args) {
        FileOperation fileOperation = new FileOperation();
        BigramLanguageModel bigramModel = fileOperation.getBigramModel();
        FindWordOperation findWordOperation = new FindWordOperation(bigramModel);
    }
}