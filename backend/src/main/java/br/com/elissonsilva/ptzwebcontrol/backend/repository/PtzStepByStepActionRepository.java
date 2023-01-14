package br.com.elissonsilva.ptzwebcontrol.backend.repository;

import br.com.elissonsilva.ptzwebcontrol.backend.entity.PtzStepByStepAction;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.PtzStepByStepTimeline;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PtzStepByStepActionRepository extends CrudRepository<PtzStepByStepAction, Long> {

}
