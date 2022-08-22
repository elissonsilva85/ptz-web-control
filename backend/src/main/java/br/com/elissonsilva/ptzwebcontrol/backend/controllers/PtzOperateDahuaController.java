package br.com.elissonsilva.ptzwebcontrol.backend.controllers;

import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerException;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.PtzSessionDahua;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamRequestSetConfig;
import br.com.elissonsilva.ptzwebcontrol.backend.services.PtzSessionManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/api/ptz/operate")
public class PtzOperateDahuaController {

    @Autowired
    private PtzSessionManagerService ptzSessionManagerService;

    @PostMapping("/{ptz}/dahua/setConfig")
    public ResponseEntity<Void> setConfig(@PathVariable("ptz") String ptz, @RequestBody List<DahuaParamRequestSetConfig> body) {
        try {
            //
            PtzSessionDahua ptzSession = (PtzSessionDahua) ptzSessionManagerService.getPtz(ptz);
            ptzSession.setConfig(body);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{ptz}/dahua/getConfig/{namesList}")
    public ResponseEntity<String> getConfig(@PathVariable("ptz") String ptz, @PathVariable("namesList") String namesList) {
        try {
            //
            List<String> list = List.of(namesList.split(","));
            //
            PtzSessionDahua ptzSession = (PtzSessionDahua) ptzSessionManagerService.getPtz(ptz);
            return new ResponseEntity<>(ptzSession.getConfig(list), HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/dahua/setTemporaryConfig")
    public ResponseEntity<Void> setTemporaryConfig(@PathVariable("ptz") String ptz, @RequestBody DahuaParamRequestSetConfig body) {
        try {
            PtzSessionDahua ptzSession = (PtzSessionDahua) ptzSessionManagerService.getPtz(ptz);
            ptzSession.setTemporaryConfig(body);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
