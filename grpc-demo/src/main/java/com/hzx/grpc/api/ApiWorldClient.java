package com.hzx.grpc.api;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;


public class ApiWorldClient {

    private final ManagedChannel channel;
    private final GreeterGrpc.GreeterBlockingStub blockingStub;


    public ApiWorldClient(String host, int port){
        channel = ManagedChannelBuilder.forAddress(host,port)
                .usePlaintext(true)
                .build();

        blockingStub = GreeterGrpc.newBlockingStub(channel);
    }


    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public  void greet(String name){
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply   response = blockingStub.sayHello(request);
        System.out.println(response.getMessage());

    }

    public static void main(String[] args) throws InterruptedException { 
        ApiWorldClient client = new ApiWorldClient("127.0.0.1",50051);
        for(int i=0;i<5;i++){
            client.greet("world:"+i);
        }


    } 
}