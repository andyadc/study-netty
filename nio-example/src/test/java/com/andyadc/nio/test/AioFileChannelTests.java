package com.andyadc.nio.test;

import com.andyadc.nio.example.utils.ByteBufferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class AioFileChannelTests {

    private static final Logger logger = LoggerFactory.getLogger(AioFileChannelTests.class);

    public static void main(String[] args) throws IOException {
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
                Paths.get("/Users/andaicheng/Workspace/temp/f1/README.md"), StandardOpenOption.READ
        );
        ByteBuffer buffer = ByteBuffer.allocate(16);
        logger.info("read start.");
        fileChannel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                logger.info("read completed");
                attachment.flip();
                ByteBufferUtil.debugAll(attachment);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
            }
        });
        logger.info("read end");

        System.in.read();
    }
}
