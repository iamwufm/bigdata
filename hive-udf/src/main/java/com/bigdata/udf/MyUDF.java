package com.bigdata.udf;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

/**
 * Date:2023/10/8
 * Author:wfm
 * Desc: 获取输入参数的长度(在hive中创建临时函数 temporary)
 *
 * package打包，把包上传到linux
 * 再hive窗口执行
 * 1.add jar /home/hadoop/hivedata/hive-udf-1.0-SNAPSHOT.jar;
 * 2.create temporary function my_len as "com.bigdata.udf.MyUDF";
 * 3.select my_len('hello');
 * 4.drop temporary function my_len;
 */
public class MyUDF extends GenericUDF {


    // 判断传进来的参数的类型和长度,约定返回的数据类型
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {

        // 判断传入的参数个数
        if (arguments.length != 1) {
            throw new UDFArgumentLengthException("请只给我一个参数");
        }

        // 判断传入参数的类型
        if (!arguments[0].getCategory().equals(ObjectInspector.Category.PRIMITIVE)) {
            throw new UDFArgumentTypeException(1, "我需要hive原始的数据类型");
        }

        // 返回int
        return PrimitiveObjectInspectorFactory.javaIntObjectInspector;
    }

    // 解决具体逻辑的
    public Object evaluate(DeferredObject[] arguments) throws HiveException {

        Object o = arguments[0].get();

        if (o == null) {
            return 0;
        }
        return o.toString().length();
    }

    // udf的执行计划
    public String getDisplayString(String[] strings) {

        // 生成HQL explain子句中显示的日志
//        return strings[0];

        // 也可以不打印，这样在explain就不会显示执行计划了
        return "";
    }
}
