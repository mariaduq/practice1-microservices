package com.example.demo;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import com.example.demo.grpc.*;

@GrpcService
public class Service extends ServiceGrpc.ServiceImplBase {

    @Override
    public void toUpperCase(Request request, StreamObserver<Response> responseObserver) {
        
        System.out.println("Request received from client:\n" + request);

        Response response = Response.newBuilder().setResult(request.getText().toUpperCase()).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
