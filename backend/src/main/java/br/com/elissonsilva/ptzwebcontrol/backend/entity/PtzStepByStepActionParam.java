package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "PtzSbsActionParam")
public class PtzStepByStepActionParam implements Serializable {

    @Id
    @GeneratedValue
    private long paramId;
    private String value;

    @ManyToOne
    @JoinColumn(name = "optionId")
    private PtzStepByStepOption option;

    @ManyToOne
    @JoinColumn(name = "actionId")
    @JsonBackReference
    private PtzStepByStepAction action;
}
