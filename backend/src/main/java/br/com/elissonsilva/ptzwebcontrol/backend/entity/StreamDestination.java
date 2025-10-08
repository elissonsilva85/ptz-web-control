package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.io.Serializable;

@Entity
@Data
@Table(name = "stream_destination")
public class StreamDestination implements Serializable {

    @Id
    @Column(name = "stream_destination_id")
    private String streamDestinationId;

    @Column(name = "label")
    private String label;

    @Column(name = "rtmp_url")
    private String rtmpUrl;

    @Column(name = "logo")
    private String logo;

    @Column(name = "active")
    private int active;

}
