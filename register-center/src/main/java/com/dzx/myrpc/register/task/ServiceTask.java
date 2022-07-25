package com.dzx.myrpc.register.task;

import com.dzx.myrpc.register.RegisterCenter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

public class ServiceTask implements Runnable {

    Socket client;
    public ServiceTask(Socket client) {
        this.client = client;
    }

    /**
     * 已经建立了socket连接  远程请求已经到达了服务端  需要调用服务端的方法得到结果 并返回给客户端
     */
    @Override
    public void run() {
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;
        /**
         * 核心处理流程
         * 1.客户端发送的object对象拿到
         * 2.采用反射机制调用服务端方法处理
         * 3.返回结果
         */
        try{
            //开启socket字节流
            inputStream = new ObjectInputStream(client.getInputStream());
            //顺序发送数据：类名、方法名、参数类型、参数值
            // 1.拿到服务名
            String serviceName = inputStream.readUTF();
            // 2.拿到方法名
            String methodName = inputStream.readUTF();
            // 3.拿到参数类型
            Class<?>[] paramTypes = (Class<?>[]) inputStream.readObject();
            // 4.拿到参数值
            Object[] arguments = (Object[]) inputStream.readObject();
            // 5.到注册中心根据接口名获取接口实现类（Map对象）
            Class serviceClass = RegisterCenter.serviceRegistry.get(serviceName);
            // 6.根据反射机制获取方法
            Method method = serviceClass.getMethod(methodName,paramTypes);
            // 7.反射地调用方法 并得到执行结果
            Object result = method.invoke(serviceClass.newInstance(), arguments);
            // 8.通过socket返回给客户端
            outputStream = new ObjectOutputStream(client.getOutputStream());
            // 9.返回结果
            outputStream.writeObject(result);

        }catch (Exception e){
            System.out.println("服务处理异常");
            e.printStackTrace();
        }finally {
            /**
             * 关闭输入输出流
             * 关闭socket连接
             */
            try {
                if(null != inputStream){
                    inputStream.close();
                }
                if(null != outputStream){
                    outputStream.close();
                }
                if(null != client){
                    client.close();
                }
            } catch (IOException e) {
                System.out.println("关闭连接异常");
                throw new RuntimeException(e);
            }
        }
    }
}
