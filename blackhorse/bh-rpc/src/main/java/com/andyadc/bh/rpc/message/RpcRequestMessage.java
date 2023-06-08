package com.andyadc.bh.rpc.message;

import java.util.Arrays;

public class RpcRequestMessage extends Message {

    private static final long serialVersionUID = 7034810083538083999L;

    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameterValues;
    private Class<?> returnType;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(Object[] parameterValues) {
        this.parameterValues = parameterValues;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    @Override
    public int getMessageType() {
        return RequestMessage;
    }

    @Override
    public String toString() {
        return "RpcRequestMessage{" +
                "className=" + className +
                ", methodName=" + methodName +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", parameterValues=" + Arrays.toString(parameterValues) +
                ", returnType=" + returnType +
                "} " + super.toString();
    }
}
