package br.com.elissonsilva.ptzwebcontrol.backend.controllers;

import br.com.elissonsilva.ptzwebcontrol.backend.entity.GenericAmount;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.JoystickRequest;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.PresetIdName;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.SpecificPositionRequest;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerException;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzJoystickDirection;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;
import br.com.elissonsilva.ptzwebcontrol.backend.services.PtzSessionManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @GetMapping("/{ptz}/preset")
    public ResponseEntity<Void> getPreset(@PathVariable("ptz") String ptz, @RequestParam("id") int id) {
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

    @PostMapping("/{ptz}/preset")
    public ResponseEntity<Void> getPreset(@PathVariable("ptz") String ptz, @RequestBody PresetIdName payload) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.savePreset(payload.getId(), payload.getName());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{ptz}/presetNames")
    public ResponseEntity<Map<Integer, String>> getPresetNames(@PathVariable("ptz") String ptz) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            return new ResponseEntity<>(ptzSession.getPresetNames(), HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{ptz}/currentPostion")
    public ResponseEntity<int[]> getCurrentPostion(@PathVariable("ptz") String ptz) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            return new ResponseEntity<>(ptzSession.getViewAngles(), HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{ptz}/zoomValue")
    public ResponseEntity<Integer> getZoomValue(@PathVariable("ptz") String ptz) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            return new ResponseEntity<>(ptzSession.getZoomValue(), HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/zoomInStart")
    public ResponseEntity<Void> zoomInStart(@PathVariable("ptz") String ptz, @RequestBody GenericAmount payload) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.startZoomIn(payload.getAmount());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/zoomInStop")
    public ResponseEntity<Void> zoomInStop(@PathVariable("ptz") String ptz, @RequestBody GenericAmount payload) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.stopZoomIn(payload.getAmount());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/zoomOutStart")
    public ResponseEntity<Void> zoomOutStart(@PathVariable("ptz") String ptz, @RequestBody GenericAmount payload) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.startZoomOut(payload.getAmount());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/zoomOutStop")
    public ResponseEntity<Void> zoomOutStop(@PathVariable("ptz") String ptz, @RequestBody GenericAmount payload) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.stopZoomOut(payload.getAmount());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/focusInStart")
    public ResponseEntity<Void> focusInStart(@PathVariable("ptz") String ptz, @RequestBody GenericAmount payload) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.startFocusIn(payload.getAmount());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/focusInStop")
    public ResponseEntity<Void> focusInStop(@PathVariable("ptz") String ptz, @RequestBody GenericAmount payload) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.stopFocusIn(payload.getAmount());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/focusOutStart")
    public ResponseEntity<Void> focusOutStart(@PathVariable("ptz") String ptz, @RequestBody GenericAmount payload) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.startFocusOut(payload.getAmount());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/focusOutStop")
    public ResponseEntity<Void> focusOutStop(@PathVariable("ptz") String ptz, @RequestBody GenericAmount payload) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.stopFocusOut(payload.getAmount());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/irisLargeStart")
    public ResponseEntity<Void> irisLargeStart(@PathVariable("ptz") String ptz, @RequestBody GenericAmount payload) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.startIrisLarge(payload.getAmount());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/irisLargeStop")
    public ResponseEntity<Void> irisLargeStop(@PathVariable("ptz") String ptz, @RequestBody GenericAmount payload) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.stopIrisLarge(payload.getAmount());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/irisSmallStart")
    public ResponseEntity<Void> irisSmallStart(@PathVariable("ptz") String ptz, @RequestBody GenericAmount payload) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.startIrisSmall(payload.getAmount());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/irisSmallStop")
    public ResponseEntity<Void> irisSmallStop(@PathVariable("ptz") String ptz, @RequestBody GenericAmount payload) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.stopIrisSmall(payload.getAmount());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{ptz}/joystickStart")
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

    @PostMapping("/{ptz}/joystickStop")
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

    @PostMapping("/{ptz}/setZoomSpeed")
    public ResponseEntity<Void> setZoomSpeed(@PathVariable("ptz") String ptz, @RequestBody GenericAmount payload) {
        try {
            PtzSessionAbstract ptzSession = ptzSessionManagerService.getPtz(ptz);
            ptzSession.setZoomSpeed(payload.getAmount());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (PtzSessionManagerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
