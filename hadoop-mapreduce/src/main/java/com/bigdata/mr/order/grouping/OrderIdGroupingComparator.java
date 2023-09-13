package com.bigdata.mr.order.grouping;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * Date:2023/9/11
 * Author:wfm
 * Desc:
 */
public class OrderIdGroupingComparator extends WritableComparator {
    public OrderIdGroupingComparator() {
        super(OrderBean.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        OrderBean a1 = (OrderBean) a;
        OrderBean b1 = (OrderBean) b;

        // 相同订单id分为一组
        return a1.getOrderId().compareTo(b1.getOrderId());
    }
}
