package com.andyadc.bh.rpc.server.service;

public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String message) {
        return "hello " + message + " " + System.currentTimeMillis();
    }
}
