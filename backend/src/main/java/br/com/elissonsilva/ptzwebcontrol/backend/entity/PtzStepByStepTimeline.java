package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "PtzSbsTimeline")
public class PtzStepByStepTimeline implements Serializable {

    @Id
    private long timelineId;

    private String ptz;
    private String label;
    //private int order;

    @OneToMany
    @JoinColumn(name = "timelineId", insertable = false, updatable = false)
    Set<PtzStepByStepAction> actions = new LinkedHashSet<>();

}
