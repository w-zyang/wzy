package hash_homework;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Scanner;

/**
 * @author wzy
 * @version 1.0
 */
public class hashset_findword {
    public static void main(String[] args) {
       String[] fileNames={"d1.txt", "d2.txt", "d3.txt"};
       HashMap<String, HashSet<Integer>> map=new HashMap<>();
       for(int i=0;i<fileNames.length;i++){
           readFile(fileNames[i],i+1,map);
       }
       Scanner sc=new Scanner(System.in);
       System.out.println("请输入要查找的单词: ");
       String word=sc.nextLine().toLowerCase();
       if(map.containsKey(word)){
           Object[] array = map.get(word).toArray();
           for(int i=0;i<array.length;i++) {
               System.out.println("d"+array[i]+".txt");
           }
       }
       else{
           System.out.println("没有找到该单词！");
       }

    }
    private static void readFile(String filename,int docId,HashMap<String,HashSet<Integer>> map ) {
        try {
            BufferedReader br=new BufferedReader(new FileReader(filename));
            String line;
            while((line=br.readLine())!=null){
                String[] words=line.toLowerCase().split("\\W+");
                for(String word:words){
                    if(!word.isEmpty()){
                        map.putIfAbsent(word, new HashSet<>());
                        map.get(word).add(docId);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
