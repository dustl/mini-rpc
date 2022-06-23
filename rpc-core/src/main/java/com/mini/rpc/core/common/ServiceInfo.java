package com.mini.rpc.core.common;

import lombok.Data;

/**
 * @Author:liwy
 * @date: 22.6.22
 * @Version:1.0
 */
@Data
public class ServiceInfo {


    /**
     * 应用名称
     * */
    private String appName;

    /**
     * 服务名称
     *
     * */
    private String serviceName;

    /**
     * 版本号
     * */
    private String version;

    /**
     * 服务地址
     *
     * */
    private String address;

    /**
     *服务端口
     *
     * */
    private Integer port;




}
