package com.dzx.myrpc.client.frame;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RpcClientFrame {
    /**
     * 远程服务的代理对象，参数为客户端需要调用的服务
     * @param serviceInterface
     */
    public static <T> T getRemoteProxyObj(final Class<?> serviceInterface) throws Exception{
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8990);
        // 1.将本地的接口调用转换成JDK的动态代理，在动态代理中实现接口的远程调用
        // 进行实际的服务调用(动态代理) jdk动态代理
        /**
         * newProxyInstance方法
         * 1.ClassLoader类加载器
         * 2.被代理的接口
         * 3.InvocationHandler接口
         */
        T t = (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[]{serviceInterface}, new DynProxy(serviceInterface, address));
        return t;
    }
    public static class DynProxy implements InvocationHandler{
        private final Class<?> serviceInterface;
        private final InetSocketAddress addr;

        public DynProxy(Class<?> serviceInterface, InetSocketAddress addr) {
            this.serviceInterface = serviceInterface;
            this.addr = addr;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Socket socket = null;
            ObjectInputStream objectInputStream = null;
            ObjectOutputStream objectOutputStream = null;
            try {

                socket = new Socket();
                socket.connect(addr);
                //输出流 向服务端发送请求
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeUTF(serviceInterface.getName());
                objectOutputStream.writeUTF(method.getName());
                objectOutputStream.writeObject(method.getParameterTypes());
                objectOutputStream.writeObject(args);
                //刷新缓冲区使数据立马发送
                objectOutputStream.flush();
                //拿到服务端返回的结果
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                System.out.println("远程调用成功！"+serviceInterface.getName());

                return objectInputStream.readObject();
            } finally {
                socket.close();
                objectOutputStream.close();
                objectInputStream.close();



            }
        }
    }
}
