package br.com.elissonsilva.ptzwebcontrol.backend.repository;

import br.com.elissonsilva.ptzwebcontrol.backend.entity.PtzName;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.PtzNameId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PtzNameRepository extends CrudRepository<PtzName, PtzNameId> {

    List<PtzName> findByPtz(String ptz);

}
