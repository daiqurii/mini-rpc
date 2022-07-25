package com.dzx.myrpc.client;

import com.dzx.myrpc.client.frame.RpcClientFrame;
import com.dzx.myrpc.server.service.OrderService;

public class Client {
    public static void main(String[] args) throws Exception {
        OrderService orderService = RpcClientFrame.getRemoteProxyObj(OrderService.class);
        System.out.println(orderService.findOrder("12345"));

    }
}
