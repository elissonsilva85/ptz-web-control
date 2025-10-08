package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "ptz_sbs_action_param")
public class PtzStepByStepParam implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "param_id")
    private long paramId;

    @Column(name = "action_id")
    private int actionId;

    @Column(name = "label")
    private String label;

    @Column(name = "short_label")
    private String shortLabel;

    @Column(name = "type")
    private String type;

    @OneToMany
    @JoinColumn(name = "param_id", insertable = false, updatable = false)
    Set<PtzStepByStepParamOption> options = new LinkedHashSet<>();
}
