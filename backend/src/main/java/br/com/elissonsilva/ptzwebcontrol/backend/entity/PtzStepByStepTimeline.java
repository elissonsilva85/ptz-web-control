package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "ptz_sbs_timeline")
public class PtzStepByStepTimeline implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timeline_id")
    private long timelineId;

    @Column(name = "ptz")
    private String ptz;

    @Column(name = "label")
    private String label;
    //private int order;

    @OneToMany
    @JoinColumn(name = "timeline_id", insertable = false, updatable = false)
    Set<PtzStepByStepAction> actions = new LinkedHashSet<>();

}
