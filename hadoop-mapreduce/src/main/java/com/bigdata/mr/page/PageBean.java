package com.bigdata.mr.page;

/**
 * Date:2023/9/7
 * Author:wfm
 * Desc:
 * <p>
 * // 不需要网络传输，所以不需要实现Writable
 */
public class PageBean implements Comparable<PageBean> {

    // 网页和浏览数
    private String page;
    private int count;

    @Override
    public int compareTo(PageBean o) {
        // 按总浏览数降序排序，如果总浏览数相等，按网页升序排序
        if (o.count - this.count == 0) {
            return this.page.compareTo(o.page);
        }
        return o.count - this.count;
    }

    public void set(String page, int count) {
        this.page = page;
        this.count = count;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
