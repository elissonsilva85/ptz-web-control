package br.com.elissonsilva.ptzwebcontrol.backend.services;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;
import br.com.elissonsilva.ptzwebcontrol.backend.udp.UdpMessageBase;
import br.com.elissonsilva.ptzwebcontrol.backend.utils.UdpMessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServerService extends Thread {

    private final static Logger logger = LoggerFactory.getLogger(UDPServerService.class);

    private final PtzSessionManagerService ptzSessionManagerService;

    private final DatagramSocket socket;

    private final String ptz;

    private boolean running;

    public UDPServerService(PtzSessionManagerService ptzSessionManagerService, String ptz, int port) throws SocketException {
        this.ptz = ptz;
        this.ptzSessionManagerService = ptzSessionManagerService;
        socket = new DatagramSocket(port);
        logger.info(ptz + " UDP Receive: Listening " + port);
    }

    public void close() {
        running = false;
        socket.close();
    }

    public void run() {
        running = true;
        byte[] buf = new byte[256];

        while (running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                logger.info(ptz + " UDP Receive: -------------------------");
            } catch (IOException e) {
                logger.info(ptz + " UDP Receive: " + e.getMessage());
                running = false;
                continue;
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            String received = UdpMessageUtils.bytesToHex(packet.getData(), packet.getLength());

            packet = new DatagramPacket(buf, buf.length, address, port);
            logger.info(ptz + " UDP Receive: msg [" + received + "]");

            try {
                if (received.equals("FF")) {
                    running = false;
                    packet.setData(new byte[0], 0, 0);
                    socket.send(packet);
                    socket.close();
                    logger.info(ptz + " UDP Receive: socket closed");
                    continue;
                }
                //
                UdpMessageBase udpMessage = UdpMessageUtils.findClass(received);
                if (udpMessage == null) {
                    logger.warn(ptz + " UDP Receive: Nenhuma classe encontrada para a mensagem [" + received + "]");
                    packet.setData(new byte[0], 0, 0);
                    socket.send(packet);
                } else {
                    //
                    PtzSessionAbstract ptzSession = null;
                    if (ptzSessionManagerService != null) {
                        ptzSession = ptzSessionManagerService.getSession(ptz);
                        if(!ptzSession.isConnected())
                            ptzSession.connect();
                    }
                    //
                    logger.info(ptz + " UDP Receive: running " + udpMessage.getName());
                    //
                    if(ptzSession != null)
                        udpMessage.doBefore(ptzSession);
                    //
                    String response = udpMessage.getResponse();
                    logger.info(ptz + " UDP Receive: response [" + response + "]");
                    byte[] tmp = UdpMessageUtils.hexStringToBytes(response);
                    packet.setData(tmp, 0, tmp.length);
                    socket.send(packet);
                    //
                    if(ptzSession != null)
                        udpMessage.doAfter(ptzSession);
                    //
                }
            } catch (Exception e) {
                logger.warn(ptz + " UDP Receive: " + e.getMessage(), e);
            }
        }
    }
}