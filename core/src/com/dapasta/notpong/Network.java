package com.dapasta.notpong;

import com.dapasta.notpong.packets.ErrorResponse;
import com.dapasta.notpong.packets.Packet;
import com.dapasta.notpong.packets.client.CreateGameRequest;
import com.dapasta.notpong.packets.client.GamesRequest;
import com.dapasta.notpong.packets.client.MovementRequest;
import com.dapasta.notpong.packets.server.CreateGameResponse;
import com.dapasta.notpong.packets.server.GamesResponse;
import com.dapasta.notpong.packets.server.MovementResponse;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.ArrayList;

public class Network {
    private Client client;

    private int timeout;
    private String host;
    private int tcpPort;
    private int udpPort;

    private int id;

    public Network(int timeout, String host, int tcpPort, int udpPort) {
        client = new Client();
        client.start();
        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                super.connected(connection);
                id = connection.getID();
            }
        });

        this.timeout = timeout;
        this.host = host;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
    }

    public boolean connect() {
        try {
            client.connect(timeout, host, tcpPort, udpPort);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        registerPackets();
        return true;
    }

    private void registerPackets() {
        Kryo kryo = client.getKryo();

        kryo.register(ArrayList.class);

        kryo.register(ErrorResponse.class);

        kryo.register(CreateGameRequest.class);
        kryo.register(CreateGameResponse.class);
        kryo.register(GamesRequest.class);
        kryo.register(GamesResponse.class);
        kryo.register(com.dapasta.notpong.packets.server.Game.class);

        kryo.register(MovementRequest.class);
        kryo.register(MovementResponse.class);
    }

    public void addListener(Listener listener) {
        client.addListener(listener);
    }

    public void removeListener(Listener listener) {
        client.removeListener(listener);
    }

    public int getId() {
        return id;
    }

    public void sendTcpPacket(Packet packet) {
        client.sendTCP(packet);
    }

    public void sendUdpPacket(Packet packet) {
        client.sendUDP(packet);
    }
}
