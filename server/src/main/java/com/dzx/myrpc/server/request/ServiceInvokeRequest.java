package com.dzx.myrpc.server.request;

import java.io.Serializable;

/**
 * 将ServiceTask中原本一个一个传递的参数封装成一个请求对象传递
 */

public class ServiceInvokeRequest implements Serializable {
    private static final long serialVersionUID = -349675930021881135L;
    private String serviceName;
    private String methodName;
    private Class<?>[] requestParamType;
    private Object[] args;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getRequestParamType() {
        return requestParamType;
    }

    public void setRequestParamType(Class<?>[] requestParamType) {
        this.requestParamType = requestParamType;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
