package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class PtzStepByStepParamOptionId implements Serializable {
    private int paramId;
    private int optionId;
}
