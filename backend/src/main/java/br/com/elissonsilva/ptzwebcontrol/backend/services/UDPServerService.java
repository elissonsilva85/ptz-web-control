package br.com.elissonsilva.ptzwebcontrol.backend.services;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class UDPServerService {

    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);

    private String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
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

    public void handleMessage(Message message)
    {
        String address = message.getHeaders().get("ip_address").toString();
        String port = message.getHeaders().get("ip_port").toString();

        String data = bytesToHex((byte[]) message.getPayload());
        //
        // Pan-tiltPosInq
        // data = "81090612FF"
        //
        System.out.print("Received data: [" + data + "] \n");
    }

}
