package br.com.elissonsilva.ptzwebcontrol.tests;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
// Removed RunWith for JUnit 5
import org.mockito.junit.MockitoJUnitRunner;

// Removed RunWith annotation for JUnit 5
public class TestsPtzDahuaParams {

    @Test
    public void testDahuaResponseError() throws JsonProcessingException {
        String json = "{\"code\":268632079,\"message\":\"Component error: login challenge!\"}";
        ObjectMapper mapper = new ObjectMapper();
        DahuaResponseError response = mapper.readValue(json, DahuaResponseError.class);
    }

    @Test
    public void testDahuaResponseLoginWithError() throws JsonProcessingException {
        String json = "{\"error\":{\"code\":268632079,\"message\":\"Component error: login challenge!\"},\"id\":4,\"params\":{\"authorization\":\"065ea7852f1ca9e4d83b1daa0843d45045d1fe57\",\"encryption\":\"Default\",\"mac\":\"errormac\",\"random\":\"172359056\",\"realm\":\"Login to 5H08A08PAJF1598\"},\"result\":false,\"session\":\"2f1ce20f10a732a3e8b49027804dd6ab\"}";
        ObjectMapper mapper = new ObjectMapper();
        DahuaResponseLogin response = mapper.readValue(json, DahuaResponseLogin.class);
    }

    @Test
    public void testDahuaResponseLogin() throws JsonProcessingException {
        String json = "{\"id\":3,\"params\":{\"keepAliveInterval\":60},\"result\":true,\"session\":\"ca8c79b073ffef65cbf98229a1ee3c6b\"}";
        ObjectMapper mapper = new ObjectMapper();
        DahuaResponseLogin response = mapper.readValue(json, DahuaResponseLogin.class);
    }

    @Test
    public void testDahuaResponseFactoryInstance() throws JsonProcessingException {
        String json = "{\"id\":4,\"result\":190176144,\"session\":\"ca8c79b073ffef65cbf98229a1ee3c6b\"}";
        ObjectMapper mapper = new ObjectMapper();
        DahuaResponseFactoryInstance response = mapper.readValue(json, DahuaResponseFactoryInstance.class);
    }

    @Test
    public void testDahuaResponseKeepAlive() throws JsonProcessingException {
        String json = "{\"id\":5,\"params\":{\"timeout\":60},\"result\":true,\"session\":\"ca8c79b073ffef65cbf98229a1ee3c6b\"}";
        ObjectMapper mapper = new ObjectMapper();
        DahuaResponseKeepAlive response = mapper.readValue(json, DahuaResponseKeepAlive.class);
    }

    @Test
    public void testDahuaResponseStart() throws JsonProcessingException {
        String json = "{\"id\":16,\"result\":true,\"session\":\"de86dccfd6c2654b714c4e8ab595ab0a\"}";
        ObjectMapper mapper = new ObjectMapper();
        DahuaResponseStartStop response = mapper.readValue(json, DahuaResponseStartStop.class);
    }

}
