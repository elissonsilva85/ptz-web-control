package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "PtzSbsAction")
public class PtzStepByStepAction implements Serializable {

    @Id
    @GeneratedValue
    private long actionId;

    private String label;
    private String description;
    private String functionName;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "actionId")
    @JsonManagedReference
    private List<PtzStepByStepActionParam> params;

}
