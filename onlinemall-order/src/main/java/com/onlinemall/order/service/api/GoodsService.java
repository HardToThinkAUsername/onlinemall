package com.onlinemall.order.service.api;

import com.onlinemall.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "onlinemall-gateway", path = "/api/item")
public interface GoodsService extends GoodsApi {
}
