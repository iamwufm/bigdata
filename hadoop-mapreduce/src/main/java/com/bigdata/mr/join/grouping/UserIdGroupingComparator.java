package com.bigdata.mr.join.grouping;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * Date:2023/9/12
 * Author:wfm
 * Desc:
 */
public class UserIdGroupingComparator extends WritableComparator {

    public UserIdGroupingComparator() {
        super(OrderUserBean.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        OrderUserBean a1 = (OrderUserBean) a;
        OrderUserBean b1 = (OrderUserBean) b;

        // 按userid分组
        return a1.getUserId().compareTo(b1.getUserId());
    }
}
