package com.andyadc.oio.example;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class OIOClient {

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 8787);
                while (true) {
                    try {
                        OutputStream outputStream = socket.getOutputStream();
                        outputStream.write(("hello io " + new Date()).getBytes(StandardCharsets.UTF_8));
                        outputStream.flush();

                        TimeUnit.SECONDS.sleep(3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
