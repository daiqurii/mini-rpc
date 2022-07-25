package com.dzx.myrpc.server;

import com.dzx.myrpc.register.RegisterCenter;
import com.dzx.myrpc.server.service.OrderService;
import com.dzx.myrpc.server.service.impl.OrderServiceImpl;

import java.io.IOException;

public class ServerRegister {
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                RegisterCenter registerCenter = new RegisterCenter(8990);
                //将服务注册到注册中心
                registerCenter.register("订单服务", OrderService.class, OrderServiceImpl.class);
                registerCenter.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
