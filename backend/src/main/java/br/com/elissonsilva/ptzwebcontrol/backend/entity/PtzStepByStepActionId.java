package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class PtzStepByStepActionId implements Serializable {
    private int timelineId;
    private int actionId;
}
