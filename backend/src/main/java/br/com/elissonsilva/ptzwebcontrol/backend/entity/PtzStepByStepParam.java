package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "ActionParam")
public class PtzStepByStepParam implements Serializable {

    @Id
    private long paramId;

    private int actionId;
    private String label;
    private String shortLabel;
    private String type;

    /*
    @OneToMany
    @JoinColumns({
        @JoinColumn(name = "paramId", insertable = false, updatable = false)
    })
    Set<PtzStepByStepParamOption> options = new LinkedHashSet<>();
    */
}