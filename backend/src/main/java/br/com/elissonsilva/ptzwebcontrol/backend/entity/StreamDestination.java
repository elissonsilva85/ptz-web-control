package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "StreamDestination")
public class StreamDestination implements Serializable {

    @Id
    private String streamDestinationId;

    private String label;

    private String rtmpUrl;

    private String logo;

    private int active;

}
