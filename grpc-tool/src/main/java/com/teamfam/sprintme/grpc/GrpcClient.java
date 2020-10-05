package com.teamfam.sprintme.grpc;

/**
 * Represent a gRpc Client used for any gRPC request/response.
 * 
 * @author teamfam
 */
public interface GrpcClient{

    /**
     * Invoke a remote method via RPC by passing in the RPC request as well as providing an RPC 
     * response.
     * 
     * @param grpcRequest The gRPC Request to make to the remote gRPC Server.
     * @param grpcResponseType The gRPC response type from the RPC server.
     */
    public <REQ,RES> RES invokeMethod(REQ grpcRequest,Class<RES> grpcResponseType);
}
