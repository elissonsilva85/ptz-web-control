package br.com.elissonsilva.ptzwebcontrol.backend.controllers;

import br.com.elissonsilva.ptzwebcontrol.backend.component.PtzSessionAbstract;
import br.com.elissonsilva.ptzwebcontrol.backend.component.PtzSessionDahua;
import br.com.elissonsilva.ptzwebcontrol.backend.component.PtzSessionManager;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
@RequestMapping("/api/ptz/operate")
public class PtzOperateDahuaController {

    @PostMapping("/{ptz}/dahua/videoColor")
    public ResponseEntity<Void> setVideoColor(@PathVariable("ptz") String ptz) {
        try {
            PtzSessionDahua ptzSession = (PtzSessionDahua) PtzSessionManager.getPtz(ptz);
            ptzSession.setVideoColor();
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
            PtzSessionDahua ptzSession = (PtzSessionDahua) PtzSessionManager.getPtz(ptz);
            ptzSession.setVideoInMode(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/dahua/videoInWhiteBalance")
    public ResponseEntity<Void> setVideoInWhiteBalance(@PathVariable("ptz") String ptz) {
        try {
            PtzSessionDahua ptzSession = (PtzSessionDahua) PtzSessionManager.getPtz(ptz);
            ptzSession.setVideoInWhiteBalance();
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
