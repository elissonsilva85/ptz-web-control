package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "PtzSbsAction")
public class PtzStepByStepAction implements Serializable {

    @Id
    @GeneratedValue
    private long actionId;

    private int executionTime;
    private String label;
    private String description;
    private String functionName;

    @Column(columnDefinition = "int default 0")
    private int executionOrder;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "actionId")
    @JsonManagedReference
    private List<PtzStepByStepParam> params;

    @ManyToOne
    @JoinColumn(name = "timelineId")
    @JsonBackReference
    private PtzStepByStepTimeline timeline;

}
