package com.mini.rpc.core.common;

/**
 * @Author:liwy
 * @date: 22.5.3
 * @Version:1.0
 */
public class ServiceUtil {

    public static String getServiceKey(String serviceName,String version){
       return String.join("-", serviceName, version);
    }
}
