package com.onlinemall.item.bo;

import com.onlinemall.item.pojo.Sku;
import com.onlinemall.item.pojo.Spu;
import com.onlinemall.item.pojo.SpuDetail;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Data
@Getter
@Setter
@ToString
public class SpuBo extends Spu {
    String cname;//商品分类名称

    String bname;//品牌名称

    SpuDetail spuDetail;//商品详情

    List<Sku> skus;//sku列表
}
