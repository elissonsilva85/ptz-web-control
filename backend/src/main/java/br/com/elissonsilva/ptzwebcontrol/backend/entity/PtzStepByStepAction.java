package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "Action")
public class PtzStepByStepAction implements Serializable {

    @Id
    private long actionId;

    private int timelineId;
    private int executionTime;
    private String label;
    private String description;
    private String functionName;

    /*
    @OneToMany
    @JoinColumns({
        @JoinColumn(name = "actionId", insertable = false, updatable = false)
    })
    Set<PtzStepByStepParam> params = new LinkedHashSet<>();
    */
}