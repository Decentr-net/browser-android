package net.decentr.module_decentr_staking.remote.utils;

import android.util.Log;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.ForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;

public class GrpcLoggingInterceptor implements ClientInterceptor {
    private static final String TAG = GrpcLoggingInterceptor.class.getSimpleName();

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(final MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {

            @Override
            public void sendMessage(ReqT message) {
                Log.v(TAG, "--> [REQUEST] " + method.getFullMethodName() + ": \n" + message.toString());
                super.sendMessage(message);
            }

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                Log.v(TAG, "--> [HEADERS] " + headers.toString()); // /!\ DO NOT LOG METADATA: causes error 400 on GCP
                ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT> listener = new
                        ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                            @Override
                            public void onMessage(RespT message) {
                                Log.v(TAG, "<-- [RESPONSE] " + method.getFullMethodName() + " (" + message.toString().length() + " bytes): \n" + message.toString());
                                super.onMessage(message);
                            }
                        };
                super.start(listener, headers);
            }
        };
    }
}
