package reactor.thread.more.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Process implements Runnable {

    private SocketChannel socketChannel;

    private ByteBuffer byteBuffer;

    public Process(SocketChannel socketChannel, ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        try {
            System.out.println("process thread:" + Thread.currentThread().getName());
            String message = new String(byteBuffer.array(), StandardCharsets.UTF_8);
            System.out.println(socketChannel.getRemoteAddress() + "发来的消息是:" + message);
            socketChannel.write(ByteBuffer.wrap("你的消息我收到了".getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}