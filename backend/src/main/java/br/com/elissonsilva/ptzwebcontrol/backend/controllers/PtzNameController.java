package br.com.elissonsilva.ptzwebcontrol.backend.controllers;

// https://www.bezkoder.com/spring-boot-jpa-crud-rest-api/

import br.com.elissonsilva.ptzwebcontrol.backend.entity.PtzName;
import br.com.elissonsilva.ptzwebcontrol.backend.repository.PtzNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/api/ptz/name")
public class PtzNameController {

    @Autowired
    private PtzNameRepository ptzNameRepository;

    @GetMapping("/{ptz}")
    public ResponseEntity<List<PtzName>> getPtzNames(@PathVariable("ptz") String ptz) {
        try {
            List<PtzName> names = new ArrayList<PtzName>();

            ptzNameRepository.findByPtz(ptz).forEach(names::add);

            if (names.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(names, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<List<PtzName>> postPtzName(@RequestBody List<PtzName> ptzNames) {
        try {
            ptzNames.forEach(ptzNameRepository::save);
            return new ResponseEntity<>(ptzNames, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
