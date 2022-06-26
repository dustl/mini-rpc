package com.mini.rpc.consumer.controller;

import com.mini.rpc.providerapi.service.HelloService;
import com.mini.rpc.rpcclient.annotation.RpcAutowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:liwy
 * @date: 22.6.25
 * @Version:1.0
 */
@RestController
@Slf4j
public class HelloController {

    @RpcAutowired(version = "1.3")
    private HelloService helloService;

    @GetMapping("/hello")
    public ResponseEntity<String> call(String word) {
        log.info("request: {}",word);
        return ResponseEntity.ok(helloService.say(word));
    }

}
