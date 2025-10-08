package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "ptz_sbs_action")
public class PtzStepByStepAction implements Serializable {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "action_id")
    private long actionId;

    @Column(name = "timeline_id")
    private int timelineId;

    @Column(name = "execution_time")
    private int executionTime;

    @Column(name = "label")
    private String label;

    @Column(name = "description")
    private String description;

    @Column(name = "function_name")
    private String functionName;

    @OneToMany
    @JoinColumn(name = "action_id", insertable = false, updatable = false)
    Set<PtzStepByStepParam> params = new LinkedHashSet<>();
}
