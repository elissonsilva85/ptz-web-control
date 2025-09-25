package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class PtzNameId implements Serializable {
    private String ptz;
    private int position;
}
