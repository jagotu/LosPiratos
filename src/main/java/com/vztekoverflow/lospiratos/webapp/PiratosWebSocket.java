package com.vztekoverflow.lospiratos.webapp;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class PiratosWebSocket extends WebSocketServer {

    public PiratosWebSocket( int port ) throws UnknownHostException {
        super( new InetSocketAddress( port ) );
    }

    public PiratosWebSocket( InetSocketAddress address ) {
        super( address );
    }


    @Override
    public void onOpen( WebSocket conn, ClientHandshake handshake ) {
        
    }

    @Override
    public void onClose( WebSocket conn, int code, String reason, boolean remote ) {

    }

    @Override
    public void onMessage( WebSocket conn, String message ) {

    }


    @Override
    public void onError( WebSocket conn, Exception ex ) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }
}
