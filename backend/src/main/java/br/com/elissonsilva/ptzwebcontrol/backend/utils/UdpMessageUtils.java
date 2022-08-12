package br.com.elissonsilva.ptzwebcontrol.backend.utils;

import br.com.elissonsilva.ptzwebcontrol.backend.udp.UdpMessageBase;
import org.reflections.Reflections;

import java.nio.charset.StandardCharsets;
import java.util.Set;

public class UdpMessageUtils {

    private final static byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);

    private final static Set<Class<? extends UdpMessageBase>> udpMessageClasses = (new Reflections("br.com.elissonsilva.ptzwebcontrol")).getSubTypesOf(UdpMessageBase.class);

    public static String bytesToHex(byte[] bytes, int length) {
        byte[] hexChars = new byte[length * 2];
        for (int j = 0; j < length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }

    public static byte[] hexStringToBytes(String s) {
        if(s == null) return new byte[0];
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static UdpMessageBase findClass(String data) {

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
                .orElse(null);
    }

}
