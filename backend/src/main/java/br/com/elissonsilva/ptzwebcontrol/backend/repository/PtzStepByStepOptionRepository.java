package br.com.elissonsilva.ptzwebcontrol.backend.repository;

import br.com.elissonsilva.ptzwebcontrol.backend.entity.PtzStepByStepAction;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.PtzStepByStepOption;
import org.springframework.data.repository.CrudRepository;

public interface PtzStepByStepOptionRepository extends CrudRepository<PtzStepByStepOption, Long> {

}
