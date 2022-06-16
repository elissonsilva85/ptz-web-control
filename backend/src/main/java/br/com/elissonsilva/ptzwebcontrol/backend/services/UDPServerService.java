package br.com.elissonsilva.ptzwebcontrol.backend.services;

import br.com.elissonsilva.ptzwebcontrol.backend.udp.UdpMessageBase;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.ip.udp.UnicastSendingMessageHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Service
public class UDPServerService {




    Logger logger = LoggerFactory.getLogger(UDPServerService.class);

    private final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);

    private final Set<Class<? extends UdpMessageBase>> udpMessageClasses = (new Reflections("br.com.elissonsilva.ptzwebcontrol")).getSubTypesOf(UdpMessageBase.class);

    private String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }

    /* s must be an even-length string. */
    private byte[] hexStringToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /*
    Received .: (5 bytes) <Buffer 81 09 06 12 ff>
    From .....: 192.168.0.101:59720
    Command ..: Pan-tiltPosInq
    Sent .....: (11 bytes) <Buffer 90 50 00 00 00 00 00 00 00 00 ff>
    Success ..: OK


    Received .: (5 bytes) <Buffer 81 09 04 47 ff>
    From .....: 192.168.0.101:59720
    Command ..: CAM_ZoomPosInq
    Sent .....: (7 bytes) <Buffer 90 50 00 00 00 00 ff>
    Success ..: OK


    Received .: (5 bytes) <Buffer 81 09 04 48 ff>
    From .....: 192.168.0.101:59720
    Command ..: CAM_FocusPosInq
    Sent .....: (7 bytes) <Buffer 90 50 00 00 00 00 ff>
    Success ..: OK


    Received .: (6 bytes) <Buffer 81 01 04 38 02 ff>
    From .....: 192.168.0.101:59720
    Command ..: Auto Focus
     */

    private UdpMessageBase findClass(String data) throws Exception {

        return udpMessageClasses
                .stream()
                .map(c -> {
                    try {
                        return (UdpMessageBase) c.getConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(udp -> {
                    udp.setData(data);
                    return udp.doFilter();
                })
                .findFirst()
                .get();
    }

    public void handleMessage(Message message)
    {
        //
        String address = message.getHeaders().get("ip_address").toString();
        int port = Integer.valueOf(message.getHeaders().get("ip_port").toString());

        //
        String data = bytesToHex((byte[]) message.getPayload());
        logger.info("Received [" + data + "]");

        //
        try {
            UdpMessageBase udpMessage = findClass(data);
            if(udpMessage == null) {
                logger.warn("handleMessage error : Nenhuma classe encontrada para a mensagem [" + data + "]");
            } else {
                udpMessage.doAction();
                this.sendResponse(address, port, udpMessage.getResponse());
            }
        } catch (Exception e) {
            logger.warn("handleMessage exception : " + e.getMessage(), e);
        }

    }

    private void sendResponse(String address, int port, String payload) {
        if(payload != null)
        {
            logger.info("sendResponse : " + address + ":" + port + " : " + payload);
            UnicastSendingMessageHandler handler =
                    new UnicastSendingMessageHandler(address, port);
            handler.handleMessage(MessageBuilder.withPayload(hexStringToBytes(payload)).build());
        }
    }

}
