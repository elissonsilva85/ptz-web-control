package br.com.elissonsilva.ptzwebcontrol.backend.controllers;

import br.com.elissonsilva.ptzwebcontrol.backend.component.PtzSessionAbstract;
import br.com.elissonsilva.ptzwebcontrol.backend.component.PtzSessionManager;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
@RequestMapping("/api/ptz/operate")
public class PtzOperateController {

    @PostMapping("/{ptz}/connect")
    public ResponseEntity<Void> connect(@PathVariable("ptz") String ptz) {
        try {
            PtzSessionManager.connect(ptz);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{ptz}/connected")
    public ResponseEntity<Boolean> connected(@PathVariable("ptz") String ptz) {
        try {
            PtzSessionAbstract ptzSession = PtzSessionManager.getPtz(ptz);
            return new ResponseEntity<>(ptzSession.isConnected(), HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{ptz}/preset/{id}")
    public ResponseEntity<Void> getPreset(@PathVariable("ptz") String ptz, @PathVariable("id") int id) {
        // LOAD
        try {
            PtzSessionAbstract ptzSession = PtzSessionManager.getPtz(ptz);
            ptzSession.loadPreset(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/preset/{id}")
    public ResponseEntity<Void> setPreset(@PathVariable("ptz") String ptz, @PathVariable("id") int id) {
        // SAVE
        try {
            PtzSessionAbstract ptzSession = PtzSessionManager.getPtz(ptz);
            ptzSession.savePreset(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/zoomIn/start")
    public ResponseEntity<Void> zoomInStart(@PathVariable("ptz") String ptz) {
        try {
            PtzSessionAbstract ptzSession = PtzSessionManager.getPtz(ptz);
            ptzSession.startZoomIn();
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/zoomIn/stop")
    public ResponseEntity<Void> zoomInStop(@PathVariable("ptz") String ptz) {
        try {
            PtzSessionAbstract ptzSession = PtzSessionManager.getPtz(ptz);
            ptzSession.stopZoomIn();
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/zoomOut/start")
    public ResponseEntity<Void> zoomOutStart(@PathVariable("ptz") String ptz) {
        try {
            PtzSessionAbstract ptzSession = PtzSessionManager.getPtz(ptz);
            ptzSession.startZoomOut();
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/zoomOut/stop")
    public ResponseEntity<Void> zoomOutStop(@PathVariable("ptz") String ptz) {
        try {
            PtzSessionAbstract ptzSession = PtzSessionManager.getPtz(ptz);
            ptzSession.stopZoomOut();
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/focusIn/start")
    public ResponseEntity<Void> focusInStart(@PathVariable("ptz") String ptz) {
        try {
            PtzSessionAbstract ptzSession = PtzSessionManager.getPtz(ptz);
            ptzSession.startFocusIn();
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/focusIn/stop")
    public ResponseEntity<Void> focusInStop(@PathVariable("ptz") String ptz) {
        try {
            PtzSessionAbstract ptzSession = PtzSessionManager.getPtz(ptz);
            ptzSession.stopFocusIn();
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/focusOut/start")
    public ResponseEntity<Void> focusOutStart(@PathVariable("ptz") String ptz) {
        try {
            PtzSessionAbstract ptzSession = PtzSessionManager.getPtz(ptz);
            ptzSession.startFocusOut();
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/focusOut/stop")
    public ResponseEntity<Void> focusOutStop(@PathVariable("ptz") String ptz) {
        try {
            PtzSessionAbstract ptzSession = PtzSessionManager.getPtz(ptz);
            ptzSession.stopFocusOut();
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/joystick/start")
    public ResponseEntity<Void> joystickStart(@PathVariable("ptz") String ptz) {
        // speed1: number, speed2: number (json body)
        String direction = "";
        int speed1 = 0;
        int speed2 = 0;
        try {
            PtzSessionAbstract ptzSession = PtzSessionManager.getPtz(ptz);
            ptzSession.startJoystick(direction, speed1, speed2);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/joystick/stop")
    public ResponseEntity<Void> joystickStop(@PathVariable("ptz") String ptz) {
        // speed1: number, speed2: number (json body)
        String direction = "";
        int speed1 = 0;
        int speed2 = 0;
        try {
            PtzSessionAbstract ptzSession = PtzSessionManager.getPtz(ptz);
            ptzSession.stopJoystick(direction, speed1, speed2);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/stopLastCall")
    public ResponseEntity<Void> stopLastCall(@PathVariable("ptz") String ptz) {
        try {
            PtzSessionAbstract ptzSession = PtzSessionManager.getPtz(ptz);
            ptzSession.stopLastCall();
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /////// UNDER DEVELOPMENT ////////////

    @PostMapping("/{ptz}/specificPosition")
    public ResponseEntity<Void> specificPosition(@PathVariable("ptz") String ptz) {
        // horizontal: number, vertical: number, zoom: number (json body)
        int horizontal = 0;
        int vertical = 0;
        int zoom = 0;
        try {
            PtzSessionAbstract ptzSession = PtzSessionManager.getPtz(ptz);
            ptzSession.specificPosition(horizontal, vertical, zoom);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{ptz}/config")
    public ResponseEntity<Void> getConfig(@PathVariable("ptz") String ptz) {
        // list: any[] (query param)
        try {
            PtzSessionAbstract ptzSession = PtzSessionManager.getPtz(ptz);
            ptzSession.getConfig();
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/config")
    public ResponseEntity<Void> setConfig(@PathVariable("ptz") String ptz) {
        // list: any[] (json body)
        try {
            PtzSessionAbstract ptzSession = PtzSessionManager.getPtz(ptz);
            ptzSession.setConfig();
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/moveDirectly")
    public ResponseEntity<Void> moveDirectly(@PathVariable("ptz") String ptz) {
        // coord: number[], speed: number (json body)
        int[] coord = new int[2];
        int speed = 0;
        try {
            PtzSessionAbstract ptzSession = PtzSessionManager.getPtz(ptz);
            ptzSession.moveDirectly(coord, speed);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
