package com.bigdata.wordcount;

/**
 * Date:2023/9/4
 * Author:wfm
 * Desc:
 */
public interface Mapper {
    public void map(String line, Context context);
}
