package com.dzx.myrpc.register;

import com.dzx.myrpc.register.task.ServiceTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterCenter {
    /*Runtime.getRuntime().availableProcessors()返回的是可用的计算资源*/
    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    /*存放服务的容器*/
    public static final HashMap<String,Class> serviceRegistry = new HashMap<String,Class>();

    private static boolean isRunning = false;

    private static int port;

    public RegisterCenter(int port) {
        this.port = port;
    }
    /**
     * 服务的注册:socket通讯+反射
     * @param serviceInterface
     * @param impl
     */
    public void register(String serviceName, Class serviceInterface, Class impl) {
        System.out.println(serviceName + "  已加入注册中心！");
        serviceRegistry.put(serviceInterface.getName(), impl);
        System.out.println("注册中心列表：" + serviceRegistry.toString());
    }
    /**
     * 启动服务注册中心
     */
    public void start() throws IOException {
        // ServerSocket IO Exception
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(port));
        System.out.println("服务中心已启动....");
        try{
            while (true){
                // 监听客户端的TCP连接，接到TCP连接后将其封装成task
                // 由线程池执行,并且同时传入socket(server.accept()=socket)
                executorService.execute(new ServiceTask(serverSocket.accept()));
            }
        }finally {
            serverSocket.close();
        }

    }
}
