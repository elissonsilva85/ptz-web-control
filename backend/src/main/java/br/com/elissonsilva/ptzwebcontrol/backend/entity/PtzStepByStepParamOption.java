package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Data
// use explicit snake_case table name to match Hibernate's default naming and generated DDL
@Table(name = "ptz_sbs_action_param_option")
public class PtzStepByStepParamOption implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private long optionId;

    @Column(name = "param_id")
    private int paramId;

    @Column(name = "type")
    private String type;

    // 'value' is a reserved word in some SQL dialects (H2). Map it to a safe column name.
    @Column(name = "opt_value")
    private String value;

}
