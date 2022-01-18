package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class PtzStepByStepParamId implements Serializable {
    private int actionId;
    private int paramId;
}