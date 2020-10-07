package com.teamfam.sprintme.grpc;

public abstract class AbstractGrpcClient implements GrpcClient {

    @Override
    public <REQ, RES> RES invokeMethod(REQ grpcRequest, Class<RES> grpcResponseType) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public abstract <STUB> STUB invokeStub(Class<STUB> stubType);
}
