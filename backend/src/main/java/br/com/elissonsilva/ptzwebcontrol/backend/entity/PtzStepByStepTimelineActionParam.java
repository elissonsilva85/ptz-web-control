package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "PtzSbsTimelineActionParam")
public class PtzStepByStepTimelineActionParam implements Serializable {

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
    private PtzStepByStepTimelineAction action;
}
