package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "PtzSbsAction")
public class PtzStepByStepAction implements Serializable {

    @Id
    @GeneratedValue
    private long actionId;

    private int timelineId;
    private int executionTime;
    private String label;
    private String description;
    private String functionName;

    @OneToMany
    @JoinColumn(name = "actionId", insertable = false, updatable = false)
    Set<PtzStepByStepParam> params = new LinkedHashSet<>();
}
