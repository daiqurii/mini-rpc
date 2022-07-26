package com.dzx.myrpc.server.handler;

import com.dzx.myrpc.server.request.ServiceInvokeRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;

/**
 * 自定义的RPC任务类
 * 客户端的请求会先经过ObjectDecoder的处理，将具体的字节数组转换为Object；
 * 后续会经过自定义的RpcObjectServerHandler，针对ServiceInvokeRequest类型的Object，则解析其具体参数，并通过反射发起调用，最后将结果值通过ctx.writeAndFlush()方法返回给客户端。
 *
 */
public class RpcObjectServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg);
        //读取到客户端的请求后 解析对应参数并通过反射发起调用
        if(msg instanceof ServiceInvokeRequest){
            ServiceInvokeRequest request = (ServiceInvokeRequest) msg;
            String serviceName = request.getServiceName();
            String methodName = request.getMethodName();
            Class<?>[] paramType = request.getRequestParamType();
            Object[] args = request.getArgs();
            //反射获取服务名
            Class<?> serviceClass = Class.forName(serviceName);
            //通过反射获取对应调用方法并获取返回值
            Method method = serviceClass.getMethod(methodName, paramType);
            Object result = method.invoke(serviceClass.newInstance(), args);
            //将结果返回给客户端
            ctx.writeAndFlush(result);

        }
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }

}
