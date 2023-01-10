package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "PtzSbsActionParam")
public class PtzStepByStepParam implements Serializable {

    @Id
    @GeneratedValue
    private long paramId;
    private String value;

    @ManyToOne
    @JoinColumn(name = "optionId", insertable = false, updatable = false)
    private PtzStepByStepOption option;

    @ManyToOne
    @JoinColumn(name = "actionId")
    @JsonBackReference
    private PtzStepByStepAction action;
}
