package com.bigdata.wordcount;

/**
 * Date:2023/9/4
 * Author:wfm
 * Desc:
 */
public class WordCountMapper implements Mapper {

    @Override
    public void map(String line, Context context) {
        // 切割
        String[] words = line.split(" ");

        for (String word : words) {
            Object value = context.get(word);

            if (value == null) {
                context.write(word, 1);
            } else {
                int v = (int) value;
                context.write(word, v + 1);
            }
        }
    }
}
