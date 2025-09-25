package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "PtzSbsActionParam")
public class PtzStepByStepParam implements Serializable {

    @Id
    @GeneratedValue
    private long paramId;

    private int actionId;
    private String label;
    private String shortLabel;
    private String type;

    @OneToMany
    @JoinColumn(name = "paramId", insertable = false, updatable = false)
    Set<PtzStepByStepParamOption> options = new LinkedHashSet<>();
}
