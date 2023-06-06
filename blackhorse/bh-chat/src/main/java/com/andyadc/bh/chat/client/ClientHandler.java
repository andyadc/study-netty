package com.andyadc.bh.chat.client;

import com.andyadc.bh.chat.message.ChatRequestMessage;
import com.andyadc.bh.chat.message.GroupChatRequestMessage;
import com.andyadc.bh.chat.message.GroupCreateRequestMessage;
import com.andyadc.bh.chat.message.GroupJoinRequestMessage;
import com.andyadc.bh.chat.message.GroupMembersRequestMessage;
import com.andyadc.bh.chat.message.GroupQuitRequestMessage;
import com.andyadc.bh.chat.message.LoginRequestMessage;
import com.andyadc.bh.chat.message.LoginResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    private final CountDownLatch latch = new CountDownLatch(1);
    private final AtomicBoolean login = new AtomicBoolean(false);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            System.out.println("username: ");
            String username = scanner.nextLine();

            System.out.println("password: ");
            String password = scanner.nextLine();

            LoginRequestMessage loginRequestMessage = new LoginRequestMessage(username, password);

            // 发送
            ctx.writeAndFlush(loginRequestMessage);

            try {
                // 等待服务端返回结果
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!login.get()) {
                ctx.channel().close();
            }

            while (true) {
                System.out.println("==========================");
                System.out.println("send [username] [content]");
                System.out.println("gsend [group name] [content]");
                System.out.println("gcreate [group name] [m1,m2,m3,...]");
                System.out.println("gmembers [group name]");
                System.out.println("gjoin [group name]");
                System.out.println("gquit [group name]");
                System.out.println("quit");
                System.out.println("==========================");
                String cmd = scanner.nextLine();
                logger.debug("received cmd: '{}'", cmd);
                String[] s = cmd.split(" ");
                switch (s[0]) {
                    case "quit":
                        ctx.channel().close();
                        return;
                    case "send":
                        ChatRequestMessage chatRequestMessage = new ChatRequestMessage(username, s[1], s[2]);
                        ctx.writeAndFlush(chatRequestMessage);
                        break;
                    case "gsend":
                        GroupChatRequestMessage groupChatRequestMessage = new GroupChatRequestMessage(username, s[1], s[2]);
                        ctx.writeAndFlush(groupChatRequestMessage);
                        break;
                    case "gcreate":
                        Set<String> members = new HashSet<>(Arrays.asList(s[2].split(",")));
                        members.add(username);
                        GroupCreateRequestMessage groupCreateRequestMessage = new GroupCreateRequestMessage(s[1], members);
                        ctx.writeAndFlush(groupCreateRequestMessage);
                        break;
                    case "gmembers":
                        GroupMembersRequestMessage groupMembersRequestMessage = new GroupMembersRequestMessage(s[1]);
                        ctx.writeAndFlush(groupMembersRequestMessage);
                        break;
                    case "gjoin":
                        GroupJoinRequestMessage groupJoinRequestMessage = new GroupJoinRequestMessage(username, s[1]);
                        ctx.writeAndFlush(groupJoinRequestMessage);
                        break;
                    case "gquit":
                        GroupQuitRequestMessage groupQuitRequestMessage = new GroupQuitRequestMessage(username, s[1]);
                        ctx.writeAndFlush(groupQuitRequestMessage);
                        break;
                    default:
                        System.out.println("x");
                        break;
                }
            }

        }, "reader").start();
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("{}", msg);
        if (msg instanceof LoginResponseMessage) {
            LoginResponseMessage loginResponseMessage = (LoginResponseMessage) msg;
            login.set(loginResponseMessage.isSuccess());
            latch.countDown();
        }
        super.channelRead(ctx, msg);
    }
}
