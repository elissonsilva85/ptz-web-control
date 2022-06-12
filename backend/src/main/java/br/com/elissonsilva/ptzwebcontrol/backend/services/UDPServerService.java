package br.com.elissonsilva.ptzwebcontrol.backend.services;

// https://medium.com/@shehanfernando/simple-udp-server-with-spring-boot-f79047eac990
// https://docs.spring.io/spring-integration/reference/html/ip.html

import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class UDPServerService
{
    public void handleMessage(Message message)
    {
        String data = new String((byte[]) message.getPayload());
        System.out.print(data);
    }
}