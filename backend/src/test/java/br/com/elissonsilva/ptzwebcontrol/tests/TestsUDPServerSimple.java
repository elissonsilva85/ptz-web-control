package br.com.elissonsilva.ptzwebcontrol.tests;

import br.com.elissonsilva.ptzwebcontrol.backend.services.UDPServerService;
import br.com.elissonsilva.ptzwebcontrol.backend.utils.UdpMessageUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestsUDPServerSimple {

    public static class EchoClient {
        private final DatagramSocket socket;
        private final InetAddress address;

        private byte[] buf;

        public EchoClient() throws SocketException, UnknownHostException {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
        }

        public String sendEcho(String msg) {
            System.out.println("sendEcho: ------------------------");
            System.out.println("sendEcho: msg [" + msg + "]");
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
            //
            byte[] tmp = UdpMessageUtils.hexStringToBytes(msg);
            packet.setData(tmp, 0, tmp.length);
            //
            try {
                socket.send(packet);
                System.out.println("sendEcho: send");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //
            packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                System.out.println("sendEcho: receive");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //
            String received = UdpMessageUtils.bytesToHex(packet.getData(), packet.getLength());
            System.out.println("sendEcho: msg [" + received + "]");
            return received;
        }

        public void close() {
            socket.close();
        }
    }

    EchoClient client;

    @BeforeEach
    public void setup() throws SocketException, UnknownHostException {
        new UDPServerService(null,"PTZ", 4445).start();
        client = new EchoClient();
    }

    @Test
    public void whenCanSendAndReceivePacket_thenCorrect() {
        String echo = client.sendEcho("81090612FF");
        assertEquals("9050" + "00000000" + "00000000" + "FF", echo);
        echo = client.sendEcho("81090447FF");
        assertEquals("9050" + "00000000" + "FF", echo);
        echo = client.sendEcho("8101040733FF");
        assertEquals("", echo);
    }

    @AfterEach
    public void tearDown() {
        client.sendEcho("FF");
        client.close();
    }
}