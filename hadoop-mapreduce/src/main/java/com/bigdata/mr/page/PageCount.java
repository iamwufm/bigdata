package com.bigdata.mr.page;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Date:2023/9/7
 * Author:wfm
 * Desc:
 */
public class PageCount implements WritableComparable<PageCount> {
    // 网页和访问次数
    private String page;
    private int count;

    @Override
    public int compareTo(PageCount o) {
        // 按访问次数降序排序，相同访问次数按网页升序排序

        if (o.count - this.count == 0) {
            return this.page.compareTo(o.page);
        }
        return o.count - this.count;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(page);
        dataOutput.writeInt(count);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        page = dataInput.readUTF();
        count = dataInput.readInt();
    }

    @Override
    public String toString() {
        return page + "," + count;
    }

    public PageCount(String page, int count) {
        this.page = page;
        this.count = count;
    }

    public PageCount() {}
}
