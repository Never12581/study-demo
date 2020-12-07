package com.hzx.grpc.oss;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.*;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.*;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.24.0)",
    comments = "Source: oss.proto")
public final class ObjectStorageServiceGrpc {

  private ObjectStorageServiceGrpc() {}

  public static final String SERVICE_NAME = "objectStorageService.ObjectStorageService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<ReqData,
      RepData> getGetStreamMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetStream",
      requestType = ReqData.class,
      responseType = RepData.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<ReqData,
      RepData> getGetStreamMethod() {
    io.grpc.MethodDescriptor<ReqData, RepData> getGetStreamMethod;
    if ((getGetStreamMethod = ObjectStorageServiceGrpc.getGetStreamMethod) == null) {
      synchronized (ObjectStorageServiceGrpc.class) {
        if ((getGetStreamMethod = ObjectStorageServiceGrpc.getGetStreamMethod) == null) {
          ObjectStorageServiceGrpc.getGetStreamMethod = getGetStreamMethod =
              io.grpc.MethodDescriptor.<ReqData, RepData>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetStream"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ReqData.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RepData.getDefaultInstance()))
              .setSchemaDescriptor(new ObjectStorageServiceMethodDescriptorSupplier("GetStream"))
              .build();
        }
      }
    }
    return getGetStreamMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ReqData,
      RepData> getSetStreamMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SetStream",
      requestType = ReqData.class,
      responseType = RepData.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<ReqData,
      RepData> getSetStreamMethod() {
    io.grpc.MethodDescriptor<ReqData, RepData> getSetStreamMethod;
    if ((getSetStreamMethod = ObjectStorageServiceGrpc.getSetStreamMethod) == null) {
      synchronized (ObjectStorageServiceGrpc.class) {
        if ((getSetStreamMethod = ObjectStorageServiceGrpc.getSetStreamMethod) == null) {
          ObjectStorageServiceGrpc.getSetStreamMethod = getSetStreamMethod =
              io.grpc.MethodDescriptor.<ReqData, RepData>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SetStream"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ReqData.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RepData.getDefaultInstance()))
              .setSchemaDescriptor(new ObjectStorageServiceMethodDescriptorSupplier("SetStream"))
              .build();
        }
      }
    }
    return getSetStreamMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ReqData,
      RepData> getAllStreamMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AllStream",
      requestType = ReqData.class,
      responseType = RepData.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<ReqData,
      RepData> getAllStreamMethod() {
    io.grpc.MethodDescriptor<ReqData, RepData> getAllStreamMethod;
    if ((getAllStreamMethod = ObjectStorageServiceGrpc.getAllStreamMethod) == null) {
      synchronized (ObjectStorageServiceGrpc.class) {
        if ((getAllStreamMethod = ObjectStorageServiceGrpc.getAllStreamMethod) == null) {
          ObjectStorageServiceGrpc.getAllStreamMethod = getAllStreamMethod =
              io.grpc.MethodDescriptor.<ReqData, RepData>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AllStream"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ReqData.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RepData.getDefaultInstance()))
              .setSchemaDescriptor(new ObjectStorageServiceMethodDescriptorSupplier("AllStream"))
              .build();
        }
      }
    }
    return getAllStreamMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ObjectStorageServiceStub newStub(io.grpc.Channel channel) {
    return new ObjectStorageServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ObjectStorageServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ObjectStorageServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ObjectStorageServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ObjectStorageServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class ObjectStorageServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getStream(ReqData request,
                          io.grpc.stub.StreamObserver<RepData> responseObserver) {
      asyncUnimplementedUnaryCall(getGetStreamMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<ReqData> setStream(
        io.grpc.stub.StreamObserver<RepData> responseObserver) {
      return asyncUnimplementedStreamingCall(getSetStreamMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<ReqData> allStream(
        io.grpc.stub.StreamObserver<RepData> responseObserver) {
      return asyncUnimplementedStreamingCall(getAllStreamMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetStreamMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                ReqData,
                RepData>(
                  this, METHODID_GET_STREAM)))
          .addMethod(
            getSetStreamMethod(),
            asyncClientStreamingCall(
              new MethodHandlers<
                ReqData,
                RepData>(
                  this, METHODID_SET_STREAM)))
          .addMethod(
            getAllStreamMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                ReqData,
                RepData>(
                  this, METHODID_ALL_STREAM)))
          .build();
    }
  }

  /**
   */
  public static final class ObjectStorageServiceStub extends io.grpc.stub.AbstractStub<ObjectStorageServiceStub> {
    private ObjectStorageServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ObjectStorageServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected ObjectStorageServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ObjectStorageServiceStub(channel, callOptions);
    }

    /**
     */
    public void getStream(ReqData request,
                          io.grpc.stub.StreamObserver<RepData> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getGetStreamMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<ReqData> setStream(
        io.grpc.stub.StreamObserver<RepData> responseObserver) {
      return asyncClientStreamingCall(
          getChannel().newCall(getSetStreamMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<ReqData> allStream(
        io.grpc.stub.StreamObserver<RepData> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getAllStreamMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class ObjectStorageServiceBlockingStub extends io.grpc.stub.AbstractStub<ObjectStorageServiceBlockingStub> {
    private ObjectStorageServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ObjectStorageServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected ObjectStorageServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ObjectStorageServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<RepData> getStream(
        ReqData request) {
      return blockingServerStreamingCall(
          getChannel(), getGetStreamMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ObjectStorageServiceFutureStub extends io.grpc.stub.AbstractStub<ObjectStorageServiceFutureStub> {
    private ObjectStorageServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ObjectStorageServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected ObjectStorageServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ObjectStorageServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_GET_STREAM = 0;
  private static final int METHODID_SET_STREAM = 1;
  private static final int METHODID_ALL_STREAM = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ObjectStorageServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ObjectStorageServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_STREAM:
          serviceImpl.getStream((ReqData) request,
              (io.grpc.stub.StreamObserver<RepData>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SET_STREAM:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.setStream(
              (io.grpc.stub.StreamObserver<RepData>) responseObserver);
        case METHODID_ALL_STREAM:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.allStream(
              (io.grpc.stub.StreamObserver<RepData>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ObjectStorageServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ObjectStorageServiceBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return OssProto.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ObjectStorageService");
    }
  }

  private static final class ObjectStorageServiceFileDescriptorSupplier
      extends ObjectStorageServiceBaseDescriptorSupplier {
    ObjectStorageServiceFileDescriptorSupplier() {}
  }

  private static final class ObjectStorageServiceMethodDescriptorSupplier
      extends ObjectStorageServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ObjectStorageServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ObjectStorageServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ObjectStorageServiceFileDescriptorSupplier())
              .addMethod(getGetStreamMethod())
              .addMethod(getSetStreamMethod())
              .addMethod(getAllStreamMethod())
              .build();
        }
      }
    }
    return result;
  }
}
