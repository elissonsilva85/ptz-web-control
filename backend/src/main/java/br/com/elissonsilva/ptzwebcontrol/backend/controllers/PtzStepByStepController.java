package br.com.elissonsilva.ptzwebcontrol.backend.controllers;

// https://www.bezkoder.com/spring-boot-jpa-crud-rest-api/

import br.com.elissonsilva.ptzwebcontrol.backend.entity.PtzStepByStepAction;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.PtzStepByStepOption;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.PtzStepByStepTimeline;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.PtzSessionDahuaKeepAlive;
import br.com.elissonsilva.ptzwebcontrol.backend.repository.PtzStepByStepActionRepository;
import br.com.elissonsilva.ptzwebcontrol.backend.repository.PtzStepByStepOptionRepository;
import br.com.elissonsilva.ptzwebcontrol.backend.repository.PtzStepByStepTimelineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@ResponseBody
@RequestMapping("/api/ptz/step")
public class PtzStepByStepController {

    private final static Logger logger = LoggerFactory.getLogger(PtzStepByStepController.class);

    @Autowired
    private PtzStepByStepTimelineRepository ptzStepByStepTimelineRepository;

    @Autowired
    private PtzStepByStepActionRepository ptzStepByStepActionRepository;

    @Autowired
    private PtzStepByStepOptionRepository ptzStepByStepOptionRepository;

    @GetMapping("/{ptz}")
    public ResponseEntity<List<PtzStepByStepTimeline>> getStepByStepTimelineListByPtz(@PathVariable("ptz") String ptz) {
        try {
            List<PtzStepByStepTimeline> list =
                    new ArrayList<>(ptzStepByStepTimelineRepository.findByPtz(ptz));

            if (list.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception " + e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/action")
    public ResponseEntity<List<PtzStepByStepAction>> getStepByStepActionList() {
        try {
            List<PtzStepByStepAction> list = new ArrayList<>();
            ptzStepByStepActionRepository.findAll().forEach(list::add);

            if (list.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception " + e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/action")
    public ResponseEntity<PtzStepByStepAction> saveAction(@RequestBody PtzStepByStepAction action) {
        try {
            PtzStepByStepAction savedAction = ptzStepByStepActionRepository.save(action);
            return new ResponseEntity<>(savedAction, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Exception " + e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/option")
    public ResponseEntity<List<PtzStepByStepOption>> getStepByStepOptionList() {
        try {
            List<PtzStepByStepOption> list = new ArrayList<>();
            ptzStepByStepOptionRepository.findAll().forEach(list::add);

            if (list.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception " + e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/option")
    public ResponseEntity<PtzStepByStepOption> saveOption(@RequestBody PtzStepByStepOption option) {
        try {
            PtzStepByStepOption savedOption = ptzStepByStepOptionRepository.save(option);
            return new ResponseEntity<>(savedOption, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Exception " + e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/timeline/{id}")
    public ResponseEntity<PtzStepByStepTimeline> getStepByStepTimeline(@PathVariable("id") Long id) {
        try {
            Optional<PtzStepByStepTimeline> item = ptzStepByStepTimelineRepository.findById(id);

            if (item.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(item.get(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception " + e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/timeline")
    public ResponseEntity<PtzStepByStepTimeline> saveTimeline(@RequestBody PtzStepByStepTimeline timeline) {
        try {
            PtzStepByStepTimeline savedTimeline = ptzStepByStepTimelineRepository.save(timeline);
            return new ResponseEntity<>(savedTimeline, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Exception " + e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

