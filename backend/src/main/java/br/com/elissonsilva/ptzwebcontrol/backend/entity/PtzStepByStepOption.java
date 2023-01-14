package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "PtzSbsOption")
public class PtzStepByStepOption implements Serializable {

    @Id
    @GeneratedValue
    private long optionId;

    private String label;
    private String shortLabel;
    private String value;

    @Enumerated(EnumType.STRING)
    private PtzStepByStepOptionType type;

}
