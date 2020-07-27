package com.onlinemall.item.pojo;

import org.springframework.http.ResponseEntity;

public class MyHttpStatus {
    public final static ResponseEntity<Void> INTERNAL_SERVER_ERROR = ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).build();
}
