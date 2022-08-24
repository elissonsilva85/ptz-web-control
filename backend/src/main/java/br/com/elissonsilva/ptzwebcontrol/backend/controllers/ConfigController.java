package br.com.elissonsilva.ptzwebcontrol.backend.controllers;

import br.com.elissonsilva.ptzwebcontrol.backend.component.Configuration;
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

@Controller
@ResponseBody
@RequestMapping("/api/config")
public class ConfigController {

    private static Logger log = LoggerFactory.getLogger(ConfigController.class);

    @Autowired
    private Configuration configuration;

    @GetMapping
    public ResponseEntity<Configuration> getConfig() throws UnknownHostException {
        return new ResponseEntity<>(configuration, HttpStatus.OK);
    }

}
