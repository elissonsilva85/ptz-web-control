package br.com.elissonsilva.ptzwebcontrol.backend.controllers;

import br.com.elissonsilva.ptzwebcontrol.backend.entity.JoystickRequest;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.SpecificPositionRequest;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerException;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzJoystickDirection;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;
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
public class PtzOperateController {

    @Autowired
    private PtzSessionManagerService ptzSessionManagerService;

    @PostMapping("/{ptz}/connect")
    public ResponseEntity<Void> connect(@PathVariable("ptz") String ptz) {
        try {
            ptzSessionManagerService.getSession(ptz);
            ptzSessionManagerService.getPtz(ptz).connect();
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
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
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
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.loadPreset(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/preset/{id}/{name}")
    public ResponseEntity<Void> setPreset(@PathVariable("ptz") String ptz, @PathVariable("id") int id, @PathVariable("name") String name) {
        // SAVE
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.savePreset(id, name);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/zoomIn/start/{amount}")
    public ResponseEntity<Void> zoomInStart(@PathVariable("ptz") String ptz, @PathVariable("amount") int amount) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.startZoomIn(amount);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/zoomIn/stop/{amount}")
    public ResponseEntity<Void> zoomInStop(@PathVariable("ptz") String ptz, @PathVariable("amount") int amount) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.stopZoomIn(amount);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/zoomOut/start/{amount}")
    public ResponseEntity<Void> zoomOutStart(@PathVariable("ptz") String ptz, @PathVariable("amount") int amount) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.startZoomOut(amount);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/zoomOut/stop/{amount}")
    public ResponseEntity<Void> zoomOutStop(@PathVariable("ptz") String ptz, @PathVariable("amount") int amount) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.stopZoomOut(amount);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/focusIn/start/{amount}")
    public ResponseEntity<Void> focusInStart(@PathVariable("ptz") String ptz, @PathVariable("amount") int amount) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.startFocusIn(amount);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/focusIn/stop/{amount}")
    public ResponseEntity<Void> focusInStop(@PathVariable("ptz") String ptz, @PathVariable("amount") int amount) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.stopFocusIn(amount);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/focusOut/start/{amount}")
    public ResponseEntity<Void> focusOutStart(@PathVariable("ptz") String ptz, @PathVariable("amount") int amount) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.startFocusOut(amount);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/focusOut/stop/{amount}")
    public ResponseEntity<Void> focusOutStop(@PathVariable("ptz") String ptz, @PathVariable("amount") int amount) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.stopFocusOut(amount);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/joystick/start")
    public ResponseEntity<Void> joystickStart(@PathVariable("ptz") String ptz, @RequestBody JoystickRequest payload) {
        // speed1: number, speed2: number (json body)
        PtzJoystickDirection direction = payload.getDirection();
        int speed1 = payload.getSpeed1();
        int speed2 = payload.getSpeed2();
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.startJoystick(direction, speed1, speed2);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/joystick/stop")
    public ResponseEntity<Void> joystickStop(@PathVariable("ptz") String ptz, @RequestBody JoystickRequest payload) {
        // speed1: number, speed2: number (json body)
        PtzJoystickDirection direction = payload.getDirection();
        int speed1 = payload.getSpeed1();
        int speed2 = payload.getSpeed2();
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
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
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.stopLastCall();
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/specificPosition")
    public ResponseEntity<Void> specificPosition(@PathVariable("ptz") String ptz, @RequestBody SpecificPositionRequest payload) {
        // horizontal: number, vertical: number, zoom: number (json body)
        int horizontal = payload.getHorizontal();
        int vertical = payload.getVertical();
        int zoom = payload.getZoom();
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.specificPosition(horizontal, vertical, zoom);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/setZoomSpeed/{amount}")
    public ResponseEntity<Void> setZoomSpeed(@PathVariable("ptz") String ptz, @PathVariable("amount") int amount) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.setZoomSpeed(amount);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /////// UNDER DEVELOPMENT ////////////

    @GetMapping("/{ptz}/config")
    public ResponseEntity<Void> getConfig(@PathVariable("ptz") String ptz) {
        // list: any[] (query param)
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.getConfig();
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/config")
    public ResponseEntity<Void> setConfig(@PathVariable("ptz") String ptz, @RequestBody List<DahuaParamRequestSetConfig> payload) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.setConfig(payload);
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
        int[] coord = new int[]{ 1, 2, 3 };
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.moveDirectly(coord);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
