package br.com.elissonsilva.ptzwebcontrol.backend.repository;

import br.com.elissonsilva.ptzwebcontrol.backend.entity.YoutubeSession;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface YoutubeSessionRepository extends CrudRepository<YoutubeSession, String> {

    List<YoutubeSession> findByCode(String code);

    YoutubeSession findFirstByOrderByCodeAsc();

}
