package br.com.elissonsilva.ptzwebcontrol.backend.controllers;

import br.com.elissonsilva.ptzwebcontrol.backend.component.Config;
import br.com.elissonsilva.ptzwebcontrol.backend.component.EnvUtil;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.PtzName;
import br.com.elissonsilva.ptzwebcontrol.backend.filters.CustomZuulFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.UnknownHostException;
import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/api/config")
public class ConfigController {

    private static Logger log = LoggerFactory.getLogger(ConfigController.class);

    @Autowired
    private EnvUtil envUtil;

    @Autowired
    private Config config;

    @GetMapping
    public ResponseEntity<Config> getConfig() throws UnknownHostException {
        String host = envUtil.getHostname();
        String port = envUtil.getPort();
        if(!"80".equals(port)) host += ":" + port;
        host = "http://" + host + "/";

        log.info("UrlBase: " + host);
        config.setUrlBase(host);

        return new ResponseEntity<>(config, HttpStatus.OK);
    }

    @GetMapping("/brand")
    public String getBrand() throws UnknownHostException {
        return config.getPtzBrand();
    }

}
