package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "PtzSbsTimelineAction")
public class PtzStepByStepTimelineAction implements Serializable {

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
    private List<PtzStepByStepTimelineActionParam> params;

    @ManyToOne
    @JoinColumn(name = "timelineId")
    @JsonBackReference
    private PtzStepByStepTimeline timeline;

}
