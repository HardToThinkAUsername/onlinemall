package com.onlinemall.search.client;

import com.github.pagehelper.PageInfo;
import com.onlinemall.OnlinemallSearchApplication;
import com.onlinemall.item.bo.SpuBo;
import com.onlinemall.item.pojo.Spu;
import com.onlinemall.search.pojo.Goods;
import com.onlinemall.search.repository.GoodsRepository;
import com.onlinemall.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OnlinemallSearchApplication.class)
public class ElasticsearchTest {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;

    @Test
    public void createIndex(){

        // 创建索引
        this.template.createIndex(Goods.class);
        // 配置映射
        this.template.putMapping(Goods.class);
        Integer pageNum = 1;
        Integer pageSize = 100;

        do {
            // 分批查询spuBo
            PageInfo<SpuBo> pageInfo = this.goodsClient.querySpuBoByPage(null, true, pageNum, pageSize);
            // 遍历spubo集合转化为List<Goods>
            List<Goods> goodsList = pageInfo.getList().stream().map(spuBo -> {
                try {
                    if(spuBo != null){
                        return this.searchService.buildGoods((Spu) spuBo);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
            for(Goods goods : goodsList){
                System.out.println(goods.getSkus());
            }
            this.goodsRepository.saveAll(goodsList);

            // 获取当前页的数据条数，如果是最后一页，没有100条
            pageSize = pageInfo.getList().size();
            // 每次循环页码加1
            pageNum++;
        } while (pageSize == 100);

    }
}