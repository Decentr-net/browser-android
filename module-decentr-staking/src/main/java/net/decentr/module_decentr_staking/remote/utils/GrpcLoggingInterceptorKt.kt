package net.decentr.module_decentr_staking.remote.utils

import android.util.Log
import io.grpc.*
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener

class GrpcLoggingInterceptorKt : ClientInterceptor {
    override fun <ReqT, RespT> interceptCall(
        method: MethodDescriptor<ReqT, RespT>,
        callOptions: CallOptions,
        next: Channel
    ): ClientCall<ReqT, RespT> {
        return object : SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            override fun sendMessage(message: ReqT) {
                Log.v(TAG, """--> [REQUEST] ${method.fullMethodName}: ${message.toString()}""".trimIndent())
                super.sendMessage(message)
            }

            override fun start(responseListener: Listener<RespT>, headers: Metadata) {
//                Log.v(TAG, "--> [HEADERS] $headers") // /!\ DO NOT LOG METADATA: causes error 400 on GCP
                val listener: SimpleForwardingClientCallListener<RespT> =
                    object : SimpleForwardingClientCallListener<RespT>(responseListener) {
                        override fun onMessage(message: RespT) {
                            Log.v(TAG, """<-- [RESPONSE] ${method.fullMethodName} (${message.toString().length} bytes): ${message.toString()}""")
                            super.onMessage(message)
                        }
                    }
                super.start(listener, headers)
            }
        }
    }

    companion object {
        private val TAG = GrpcLoggingInterceptor::class.java.simpleName
    }
}