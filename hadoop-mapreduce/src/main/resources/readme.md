### 4.1 map阶段

#### 4.1.1 读数据：InputFormat(输入数据接口)

（1）默认使用的实现类是==TextInputFormat==（功能逻辑是：一次读一行文本，key：这行起始字节偏移量（LongWritable类型）；value：这行的内容（Text类型））。
代码：com.bigdata.mr.wordCount.JobSubmitterWindowsLocal

（2）==SequenceFileInputFormat==：读Sequence文件（key：存储的key的数据；value：存储的value的数据）。
代码：com.bigdata.mr.index.sequence.IndexStepTwoSeqMain

（3）==CombineTextInputFormat==：用于小文件过多的场景，它可以将多个小文件从逻辑上规划到一个切片中，这样，多个小文件就可以交给一个 MapTask 处理。（key：这行起始字节偏移量（LongWritable类型）；value：这行的内容（Text类型））。
代码：com.bigdata.mr.wordCount.WordCountCombineTextMain

（4）DBInputFormat：读数据库

（5）KeyValueTextInputFormat：（key：这行的内容（Text类型）；value：空（Text类型））。
代码：com.bigdata.mr.wordCount.WordCountKeyVlaueTextMain

（7）NLineInputFormat：按行切片（key：这行的内容（Text类型）；value：空（Text类型））。
代码：com.bigdata.mr.wordCount.WordCountNLineMain

```java
// 设置输入格式 ，输入文件格式为Sequence。默认为TextInputFormat
job.setInputFormatClass(SequenceFileInputFormat.class);

// -----可以把多个小文件合并成一个切片处理，提高处理效率。----
// 如果不设置InputFormat，它默认用的是TextInputFormat.class
job.setInputFormatClass(CombineTextInputFormat.class);
// 虚拟存储切片最大值设置4m
CombineTextInputFormat.setMaxInputSplitSize(job, 4194304);
```

```java
// 根据文件类型获取切片信息
FileSplit inputSplit = (FileSplit) context.getInputSplit();
// 获取切片的文件名称
String fileName = inputSplit.getPath().getName();
```
#### 4.1.2 处理数据：Mapper(逻辑处理接口)

用户根据业务需求重写其中三个方法：setup() map()  cleanup ()

setup() 在执行map()前执行一次，可以做一些初始化的工作。
如如com.bigdata.mr.index.IndexStepOneMain、com.bigdata.mr.join.mapjoin.MapJoinMain

==map()方法实现对数据的处理==

cleanup()在map()后执行一次

```java
job.setMapperClass(WordcountMapper.class);
```
#### 4.1.3 分区：Partitioner

将map阶段产生的key-value数据，分发给若干个reduce task来分担负载，maptask调用Partition类的getPartition()方法来决定如何划分数据给不同的reduce task

有默认实现 HashPartitioner，逻辑是根据 key 的哈希值和 numReduces取模来返回一个分区号；(key.hashCode() & Integer.MAX_VALUE) % numReduceTasks

可以自定义分区，参见com.bigdata.mr.flow.FlowSumPartitionsMain

```java
// 设置参数：maptask在做数据分区时，用哪个分区逻辑类  （如果不指定，它会用默认的HashPartitioner）
 job.setPartitionerClass(ProvincePartitioner.class);
```
#### 4.1.4 排序：Comparable

对key-value数据做排序：调用key.compareTo()方法来实现对key-value数据排序

当我们需要对自定义的对象的进行排序时，就必须要实现 Comparable 接口，重写其中的 compareTo()方法。所有的数据类型默认是按照字典顺序排序的。

自定义排序参见om.bigdata.mr.page.PageCountSortMain,com.bigdata.mr.page.PageTop3Main,com.bigdata.mr.page.PageTopNMain

```java
public class PageBean implements Comparable<PageBean> {
	@Override  
	public int compareTo(PageCount o) {  
		// 按访问次数降序排序，相同访问次数按网页升序排序  
		if (o.count - this.count == 0) {  
			return this.page.compareTo(o.page);  
		}  
		return o.count - this.count;  
	}
}
```
#### 4.1.5 合并：Combiner

Combiner 合并可以提高程序执行效率，减少 IO 传输。但是使用时必须不能影响原有的业务处理结果。

参见com.bigdata.mr.wordCount.JobSubmitterWindowsLocal

```java
// 设置maptask端的局部聚合逻辑类
job.setCombinerClass(WordcountReducer.class);
```
### 4.2 reduce阶段

#### 4.2.1 读数据

通过http方式从maptask产生的数据文件中下载属于自己的“区”的数据到本地磁盘，然后将多个“同区文件”做合并（归并排序）
#### 4.2.2 分组：GroupingComparator

通过调用GroupingComparator的compare()方法来判断文件中的哪些key-value属于同一组，然后将这一组数据传给Reduce类的reduce()方法聚合一次

参见com.bigdata.mr.order.grouping.OrderTopnMain

```java
job.setGroupingComparatorClass(OrderIdGroupingComparator.class);
```

#### 4.2.3 处理数据 ：Reducer(逻辑处理接口)

用户根据业务需求实现其中三个方法：setup() reduce()  cleanup ()

setup() 在执行reduce()前执行一次，可以做一些初始化的工作。

==reduce()方法实现对数据的处理==

cleanup()在reduce()后执行一次

```java
job.setReducerClass(WordcountReducer.class);
```
#### 4.2.4 输出结果：OutputFormat(输出数据接口)

（1）默认实现类是TextOutputFormat（功能逻辑是：将每一个 KV 对，向目标文本文件输出一行）

（2）SequenceFileOutputFormat 写Sequence文件（直接将key-value对象序列化到文件中）

（3）DBOutputFormat 写数据库

参见com.bigdata.mr.index.IndexStepOneMain

```java
// 设置输出格式，输出文件格式为Sequence。默认为TextInputFormat
job.setOutputFormatClass(SequenceFileOutputFormat.class);
```
