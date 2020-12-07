package com.hzx.grpc.oss;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class OssClient {

    private final ManagedChannel channel;
    private final ObjectStorageServiceGrpc.ObjectStorageServiceBlockingStub blockingStub;


    public OssClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .build();

        blockingStub = ObjectStorageServiceGrpc.newBlockingStub(channel);
    }


    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void getStream(String name) {
        Iterator<RepData> it = blockingStub.getStream(ReqData.newBuilder().setData(name).build());
        while (it.hasNext()) {
            System.out.println(it.next().getData());
        }
    }

    public void setStream() {

    }

    public static void main(String[] args) throws InterruptedException {
        OssClient client = new OssClient("127.0.0.1", 9000);
        client.getStream("hello");

    }
}
