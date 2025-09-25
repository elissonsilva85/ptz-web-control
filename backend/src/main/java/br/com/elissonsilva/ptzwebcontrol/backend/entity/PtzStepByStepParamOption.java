package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "PtzSbsActionParamOption")
public class PtzStepByStepParamOption implements Serializable {

    @Id
    @GeneratedValue
    private long optionId;

    private int paramId;
    private String type;
    private String value;

}
