package com.bigdata.elasticSearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Buckets;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.mapping.SourceField;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Date:2023/10/26
 * Author:wfm
 * Desc: es查询
 * 使用单元测试包（junit）来操作es。
 *  点击对应test的方法，都先执行before,点击的test,after
 * <p>
 * 索引要求：
 * 分片:2 副本：2
 * 默认分词器(搜索也会用指定默认的分词器)：ik_smart（也可以在elasticsearch.yml文件配置）
 * empid:员工id-->long 不分词
 * age:年龄-->integer 不分词
 * balance:收入-->float 不建索引
 * name:姓名-->text 分词
 * gender:性别-->keyword 不分词
 * hobby:兴趣爱好-->text 分词
 * tag:标签 -->text 分词 不存储 用标准分词器
 * <p>
 * 创建索引
 * 批量添加文档
 * 1.全表查询，按照年龄降序排序，再按照工资降序排序，只取前5条记录的empid，age，balance
 * 2.搜索hobby含有吃饭睡觉的员工
 * 3.搜索工资是2000的员工
 * 4.搜索hobby是“吃饭睡觉”的员工
 * 5.搜索name或hobby中带球的员工
 * 6.搜索男性中喜欢购物的员工
 * 7.搜索男性中喜欢购物，还不能爱去酒吧的员工
 * 8.搜索男性中喜欢购物，还不能爱去酒吧的员工，年龄最好在20-30之间
 * 9.搜索男性中喜欢购物，还不能爱去酒吧的员工，最好在20-30之间，不要40岁以上的
 * 10.搜索男性中喜欢购物 或 年龄超过39岁的人
 * 11.模糊音匹配:搜索Nick
 * 12.统计男女员工各多少人
 * 13.统计喜欢购物的男女员工各多少人
 */
public class EsQueryMain {
    private RestClient restClient = null;
    private ElasticsearchTransport transport = null;
    private ElasticsearchClient client = null;
    private StringTermsBucket stringTermsBucket;


    @Before
    public void init() throws Exception {
        // 创建ES客户端部分
        restClient = RestClient.builder(
                new HttpHost("hadoop101", 9200)
                , new HttpHost("hadoop102", 9200)
                , new HttpHost("hadoop103", 9200)).build();
        transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        client = new ElasticsearchClient(transport);
    }

    // 创建索引
    @Test
    public void createIndex() throws IOException {
        // 分片:2 副本：2
        CreateIndexResponse response = client.indices().create(builder -> builder
                .settings(indexSettingsBuilder -> indexSettingsBuilder
                        .numberOfReplicas("2")
                        .numberOfShards("2"))
                .mappings(typeMappingBuilder -> typeMappingBuilder
                        .source(new SourceField.Builder().excludes("tag").build()) // 不保存tag的数据
                        .properties("empid", propertyBuilder -> propertyBuilder.long_(unsignedLongNumberPropertyBuilder -> unsignedLongNumberPropertyBuilder))
                        .properties("age", propertyBuilder -> propertyBuilder.integer(integerNumberPropertyBuilder -> integerNumberPropertyBuilder))
                        .properties("balance", propertyBuilder -> propertyBuilder.float_(floatNumberPropertyBuilder -> floatNumberPropertyBuilder.index(false)))
                        .properties("name", propertyBuilder -> propertyBuilder.text(textPropertyBuilder -> textPropertyBuilder.analyzer("ik_smart").searchAnalyzer("ik_smart")))
                        .properties("gender", propertyBuilder -> propertyBuilder.keyword(keywordPropertyBuilder -> keywordPropertyBuilder))
                        .properties("hobby", propertyBuilder -> propertyBuilder.text(textPropertyBuilder -> textPropertyBuilder.analyzer("ik_smart").searchAnalyzer("ik_smart")))
                        .properties("tag", propertyBuilder -> propertyBuilder.text(textPropertyBuilder -> textPropertyBuilder.analyzer("standard").searchAnalyzer("standard")))
                )
                .index("myusers"));

        System.out.println(response.acknowledged());

        // 关闭客户端连接部分
        transport.close();
        restClient.close();
    }

    // 批量添加文档
    @Test
    public void testCreate() throws Exception {
        // 构建一个批量操作BulkOperation的集合
        List<BulkOperation> bulkOperations = new ArrayList<>();
        // 向集合添加数据
        bulkOperations.add(new BulkOperation.Builder().create(d -> d.document(new MyUsers(1001, 20, 2000, "李三", "男", "吃饭睡觉")).id("1")).build());
        bulkOperations.add(new BulkOperation.Builder().create(d -> d.document(new MyUsers(1002, 30, 2600, "李小三", "男", "吃粑粑睡觉")).id("2")).build());
        bulkOperations.add(new BulkOperation.Builder().create(d -> d.document(new MyUsers(1003, 35, 2900, "张伟", "女", "吃,睡觉")).id("3")).build());
        bulkOperations.add(new BulkOperation.Builder().create(d -> d.document(new MyUsers(1004, 40, 2600, "张伟大", "男", "打篮球睡觉")).id("4")).build());
        bulkOperations.add(new BulkOperation.Builder().create(d -> d.document(new MyUsers(1005, 23, 2900, "大张伟", "女", "打乒乓球睡觉")).id("5")).build());
        bulkOperations.add(new BulkOperation.Builder().create(d -> d.document(new MyUsers(1006, 26, 2700, "张大喂", "男", "打排球睡觉")).id("6")).build());
        bulkOperations.add(new BulkOperation.Builder().create(d -> d.document(new MyUsers(1007, 29, 3000, "王五", "女", "打牌睡觉")).id("7")).build());
        bulkOperations.add(new BulkOperation.Builder().create(d -> d.document(new MyUsers(1008, 28, 3000, "王武", "男", "打桥牌")).id("8")).build());
        bulkOperations.add(new BulkOperation.Builder().create(d -> d.document(new MyUsers(1009, 32, 32000, "王小五", "男", "喝酒,吃烧烤")).id("9")).build());
        bulkOperations.add(new BulkOperation.Builder().create(d -> d.document(new MyUsers(1010, 37, 3600, "赵六", "男", "吃饭喝酒")).id("10")).build());
        bulkOperations.add(new BulkOperation.Builder().create(d -> d.document(new MyUsers(1011, 39, 3500, "张小燕", "女", "逛街,购物,买")).id("11")).build());
        bulkOperations.add(new BulkOperation.Builder().create(d -> d.document(new MyUsers(1012, 42, 3500, "李三", "男", "逛酒吧,购物")).id("12")).build());
        bulkOperations.add(new BulkOperation.Builder().create(d -> d.document(new MyUsers(1013, 42, 3400, "李球", "男", "体育场,购物")).id("13")).build());
        bulkOperations.add(new BulkOperation.Builder().create(d -> d.document(new MyUsers(1014, 22, 3400, "李健身", "男", "体育场,购物")).id("14")).build());
        bulkOperations.add(new BulkOperation.Builder().create(d -> d.document(new MyUsers(1015, 22, 3400, "Nick", "男", "坐飞机,购物")).id("15")).build());

        BulkResponse response = client.bulk(e -> e.index("myusers").operations(bulkOperations));
        // 打印结果
        System.out.println(response.took());
        System.out.println(response.items());

        // 关闭客户端连接部分
        transport.close();
        restClient.close();

    }

    // 全表查询，按照年龄降序排序，再按照工资降序排序，只取前5条记录的empid，age，balance
    @Test
    public void testQuery1() throws Exception {
        SearchResponse<MyUsers> search = client.search(s -> s
                        .index("myusers")
                        .sort(sort -> sort.field(f -> f.field("age").order(SortOrder.Desc)))
                        .sort(sort -> sort.field(f -> f.field("balance").order(SortOrder.Desc)))
                        .source(source -> source.filter(f -> f.includes("empid", "age", "balance")))
                , MyUsers.class);

        System.out.println(search.took());
        System.out.println("总条数" + search.hits().total().value());
        search.hits().hits().forEach(e -> System.out.println(e.source().toString()));

        // 关闭客户端连接部分
        transport.close();
        restClient.close();
    }

    // 搜索hobby含有吃饭睡觉的员工
    @Test
    public void testQuery2() throws Exception {
        SearchResponse<MyUsers> search = client.search(s -> s
                        .index("myusers")
                        .query(q -> q.match(m -> m.field("hobby").query("吃饭睡觉")))
                , MyUsers.class);

        System.out.println(search.took());
        System.out.println("总条数" + search.hits().total().value());
        search.hits().hits().forEach(e -> System.out.println(e.source().toString()));

        // 关闭客户端连接部分
        transport.close();
        restClient.close();
    }

    // 搜索工资是2000的员工
    @Test
    public void testQuery3() throws Exception {
        SearchResponse<MyUsers> search = client.search(s -> s
                        .index("myusers")
                        .query(q -> q.match(m -> m.field("balance").query("2000")))
                , MyUsers.class);

        System.out.println(search.took());
        System.out.println("总条数" + search.hits().total().value());
        search.hits().hits().forEach(e -> System.out.println(e.source().toString()));

        // 关闭客户端连接部分
        transport.close();
        restClient.close();
    }

    // 搜索hobby是“吃饭睡觉”的员工
    @Test
    public void testQuery4() throws Exception {
        SearchResponse<MyUsers> search = client.search(s -> s
                        .index("myusers")
                        .query(q -> q.matchPhrase(m -> m.field("hobby").query("吃饭睡觉"))) // 吃饭睡觉部分词，当作一个词搜索
                , MyUsers.class);

        System.out.println(search.took());
        System.out.println("总条数" + search.hits().total().value());
        search.hits().hits().forEach(e -> System.out.println(e.source().toString()));

        // 关闭客户端连接部分
        transport.close();
        restClient.close();
    }

    // 搜索name或hobby中带球的员工
    @Test
    public void testQuery5() throws Exception {
        SearchResponse<MyUsers> search = client.search(s -> s
                        .index("myusers")
                        .query(q -> q.multiMatch(m -> m.fields("name", "hobby").query("球")))
                , MyUsers.class);

        System.out.println(search.took());
        System.out.println("总条数" + search.hits().total().value());
        search.hits().hits().forEach(e -> System.out.println(e.source().toString()));

        // 关闭客户端连接部分
        transport.close();
        restClient.close();
    }

    // 搜索男性中喜欢购物的员工
    @Test
    public void testQuery6() throws Exception {
        SearchResponse<MyUsers> search = client.search(s -> s
                        .index("myusers")
                        .query(q -> q
                                .bool(b -> b
                                        .must(m -> m
                                                .term(t -> t
                                                        .field("gender")
                                                        .value("男")
                                                )
                                        )
                                        .must(m -> m
                                                .match(ma -> ma
                                                        .field("hobby")
                                                        .query("购物")))

                                ))
                , MyUsers.class);

        System.out.println(search.took());
        System.out.println("总条数" + search.hits().total().value());
        search.hits().hits().forEach(e -> System.out.println(e.source().toString()));

        // 关闭客户端连接部分
        transport.close();
        restClient.close();
    }

    // 搜索男性中喜欢购物，还不能爱去酒吧的员工
    @Test
    public void testQuery7() throws Exception {
        SearchResponse<MyUsers> search = client.search(s -> s
                        .index("myusers")
                        .query(q -> q
                                .bool(b -> b
                                        .must(m -> m
                                                .term(t -> t
                                                        .field("gender")
                                                        .value("男")
                                                )
                                        )
                                        .must(m -> m
                                                .match(ma -> ma
                                                        .field("hobby")
                                                        .query("购物")))
                                        .mustNot(mn -> mn
                                                .match(ma -> ma
                                                        .field("hobby")
                                                        .query("酒吧")))

                                ))
                , MyUsers.class);

        System.out.println(search.took());
        System.out.println("总条数" + search.hits().total().value());
        search.hits().hits().forEach(e -> System.out.println(e.source().toString()));

        // 关闭客户端连接部分
        transport.close();
        restClient.close();
    }

    // 搜索男性中喜欢购物，还不能爱去酒吧的员工，年龄最好在20-30之间
    // 20-30之间的排在前面
    @Test
    public void testQuery8() throws Exception {
        SearchResponse<MyUsers> search = client.search(s -> s
                        .index("myusers")
                        .query(q -> q
                                .bool(b -> b
                                        .must(m -> m
                                                .term(t -> t
                                                        .field("gender")
                                                        .value("男")
                                                )
                                        )
                                        .must(m -> m
                                                .match(ma -> ma
                                                        .field("hobby")
                                                        .query("购物")))
                                        .mustNot(mn -> mn
                                                .match(ma -> ma
                                                        .field("hobby")
                                                        .query("酒吧")))
                                        .should(sh -> sh
                                                .range(ra -> ra
                                                        .field("age")
                                                        .gt(JsonData.of(20))
                                                        .lt(JsonData.of(30))))

                                ))
                , MyUsers.class);

        System.out.println(search.took());
        System.out.println("总条数" + search.hits().total().value());
        search.hits().hits().forEach(e -> System.out.println(e.source().toString()));

        // 关闭客户端连接部分
        transport.close();
        restClient.close();
    }

    // 搜索男性中喜欢购物，还不能爱去酒吧的员工，最好在20-30之间，不要40岁以上的
    // 20-30之间的排在前面
    @Test
    public void testQuery9() throws Exception {
        SearchResponse<MyUsers> search = client.search(s -> s
                        .index("myusers")
                        .query(q -> q
                                .bool(b -> b
                                        .must(m -> m
                                                .term(t -> t
                                                        .field("gender")
                                                        .value("男")
                                                )
                                        )
                                        .must(m -> m
                                                .match(ma -> ma
                                                        .field("hobby")
                                                        .query("购物")))
                                        .mustNot(mn -> mn
                                                .match(ma -> ma
                                                        .field("hobby")
                                                        .query("酒吧")))
                                        .mustNot(mn -> mn
                                                .range(r -> r
                                                        .field("age")
                                                        .gt(JsonData.of(40))))
                                        .should(sh -> sh
                                                .range(ra -> ra
                                                        .field("age")
                                                        .gt(JsonData.of(20))
                                                        .lt(JsonData.of(30))))

                                ))
                , MyUsers.class);

        System.out.println(search.took());
        System.out.println("总条数" + search.hits().total().value());
        search.hits().hits().forEach(e -> System.out.println(e.source().toString()));

        // 关闭客户端连接部分
        transport.close();
        restClient.close();
    }

    // 搜索男性中喜欢购物 或 年龄超过39岁的人
    @Test
    public void testQuery10() throws Exception {
        SearchResponse<MyUsers> search = client.search(s -> s
                        .index("myusers")
                        .query(q -> q
                                .bool(b -> b
                                        .should(s1 -> s1
                                                .bool(b1 -> b1
                                                        .must(m1 -> m1
                                                                .term(t1 -> t1
                                                                        .field("gender")
                                                                        .value("男"))
                                                        )
                                                        .must(m2 -> m2
                                                                .match(t2 -> t2
                                                                        .field("hobby")
                                                                        .query("购物"))
                                                        )
                                                )
                                        )
                                        .should(s2 -> s2
                                                .range(r1 -> r1
                                                        .field("age")
                                                        .gt(JsonData.of(39)))
                                        )
                                )
                        )
                , MyUsers.class);

        System.out.println(search.took());
        System.out.println("总条数" + search.hits().total().value());
        search.hits().hits().forEach(e -> System.out.println(e.source().toString()));

        // 关闭客户端连接部分
        transport.close();
        restClient.close();
    }

    // 模糊音匹配
    // 搜索Nick
    @Test
    public void testQuery11() throws Exception {
        SearchResponse<MyUsers> search = client.search(s -> s
                        .index("myusers")
                        .query(q -> q
                                .fuzzy(f -> f
                                        .field("name")
                                        .value("Dick"))
                        )
                , MyUsers.class);

        System.out.println(search.took());
        System.out.println("总条数" + search.hits().total().value());
        search.hits().hits().forEach(e -> System.out.println(e.source().toString()));

        // 关闭客户端连接部分
        transport.close();
        restClient.close();
    }

    // 统计男女员工各多少人
    @Test
    public void testQuery12() throws Exception {
        SearchResponse<MyUsers> search = client.search(s -> s
                        .index("myusers")
                        .aggregations("counts", a -> a
                                .terms(t -> t
                                        .field("gender"))
                        )
                , MyUsers.class);

        for (Map.Entry<String, Aggregate> entry : search.aggregations().entrySet()) {
            Buckets<StringTermsBucket> buckets = entry.getValue().sterms().buckets();
            for (StringTermsBucket stringTermsBucket : buckets.array()) {
                System.out.println(stringTermsBucket.key()._toJsonString() + "-->" + stringTermsBucket.docCount());
            }
        }

        // 关闭客户端连接部分
        transport.close();
        restClient.close();
    }

    // 统计喜欢购物的男女员工各多少人
    @Test
    public void testQuery13() throws Exception {
        SearchResponse<MyUsers> search = client.search(s -> s
                        .index("myusers")
                        .query(q -> q
                                .match(m -> m
                                        .field("hobby")
                                        .query("购物")))
                        .aggregations("counts", a -> a
                                .terms(t -> t
                                        .field("gender"))
                        )
                , MyUsers.class);

        for (Map.Entry<String, Aggregate> entry : search.aggregations().entrySet()) {
            Buckets<StringTermsBucket> buckets = entry.getValue().sterms().buckets();
            for (StringTermsBucket stringTermsBucket : buckets.array()) {
                System.out.println(stringTermsBucket.key()._toJsonString() + "-->" + stringTermsBucket.docCount());

            }
        }

        // 关闭客户端连接部分
        transport.close();
        restClient.close();
    }

}
