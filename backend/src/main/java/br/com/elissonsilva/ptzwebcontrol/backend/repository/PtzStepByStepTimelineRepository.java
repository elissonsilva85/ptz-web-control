package br.com.elissonsilva.ptzwebcontrol.backend.repository;

import br.com.elissonsilva.ptzwebcontrol.backend.entity.PtzStepByStepTimeline;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PtzStepByStepTimelineRepository extends CrudRepository<PtzStepByStepTimeline, Long> {

    List<PtzStepByStepTimeline> findByPtz(String ptz);

}
