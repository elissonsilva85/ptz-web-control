package br.com.elissonsilva.ptzwebcontrol.backend.controllers;

import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerException;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.PtzSessionDahua;
import br.com.elissonsilva.ptzwebcontrol.backend.services.PtzSessionManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
@RequestMapping("/api/ptz/operate")
public class PtzOperateDahuaController {

    @Autowired
    private PtzSessionManagerService ptzSessionManagerService;

    @PostMapping("/{ptz}/dahua/videoColor")
    public ResponseEntity<Void> setVideoColor(@PathVariable("ptz") String ptz) {
        try {
            PtzSessionDahua ptzSession = (PtzSessionDahua) ptzSessionManagerService.getPtz(ptz);
            ptzSession.setVideoColor(false);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/dahua/videoInMode/{id}")
    public ResponseEntity<Void> setVideoInMode(@PathVariable("ptz") String ptz, @PathVariable("id") int id) {
        try {
            PtzSessionDahua ptzSession = (PtzSessionDahua) ptzSessionManagerService.getPtz(ptz);
            ptzSession.setVideoInMode(id, false);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/dahua/videoInWhiteBalance")
    public ResponseEntity<Void> setVideoInWhiteBalance(@PathVariable("ptz") String ptz) {
        //

        //
        try {
            PtzSessionDahua ptzSession = (PtzSessionDahua) ptzSessionManagerService.getPtz(ptz);
            ptzSession.setVideoInWhiteBalance(false);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
