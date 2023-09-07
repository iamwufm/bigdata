package com.bigdata.mr.flow;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Date:2023/9/7
 * Author:wfm
 * Desc:
 */
public class TreeMapTest {

    public static void main(String[] args) {

        // 按总流量降序

        TreeMap<FlowBean, String> tm1 = new TreeMap<>(new Comparator<FlowBean>() {
            @Override
            public int compare(FlowBean o1, FlowBean o2) {
                // 升序 o1.getAmountFlow() - o2.getAmountFlow()

                // 如果总流量相同，按手机号码降序
                if (o2.getAmountFlow() - o1.getAmountFlow() == 0){
                    return o2.getPhoneNum().compareTo(o1.getPhoneNum());
                }

                return o2.getAmountFlow() - o1.getAmountFlow();
            }
        });


        FlowBean b1 = new FlowBean("1367788", 500, 300);
        FlowBean b2 = new FlowBean("1367766", 400, 200);
        FlowBean b3 = new FlowBean("1367755", 600, 400);
        FlowBean b4 = new FlowBean("1367744", 300, 500);

        tm1.put(b1, null);
        tm1.put(b2, null);
        tm1.put(b3, null);
        tm1.put(b4, null);

        Set<FlowBean> flowBeans = tm1.keySet();

        for (FlowBean flowBean : flowBeans) {
            System.out.println(flowBean);
        }


    }
}
