package com.onlinemall.auth.client;

import com.onlinemall.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "user-service", url = "api.onlinemall.com/api/")
public interface UserClient extends UserApi {
}
