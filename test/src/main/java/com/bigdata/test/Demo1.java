package com.bigdata.test;



import org.apache.commons.io.FileUtils;
import org.apache.directory.api.util.OsgiUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Date:2023/9/2
 * Author:wfm
 * Desc:
 */
public class Demo1 {
    public static void main(String[] args) throws Exception {
        String dd = "/locks/seq-0000000018";

        String substring = dd.substring("/locks/".length());
        System.out.println(substring);

    }


}
