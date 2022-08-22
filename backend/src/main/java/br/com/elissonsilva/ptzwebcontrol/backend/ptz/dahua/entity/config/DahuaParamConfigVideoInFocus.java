package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DahuaParamConfigVideoInFocus extends DahuaParamConfigAbstract {

    public enum Mode {
        Automatico(2),
        Semiautomatico(3),
        Manual(4);

        private final int valor;

        Mode(int valor) {
            this.valor = valor;
        }

        public int getValue() {
            return this.valor;
        }


    }

    @JsonProperty("AutoFocusTrace")
    private int autoFocusTrace = 1;

    @JsonProperty("FocusLimit")
    private int focusLimit = 5000;

    @JsonProperty("FocusLimitSelectMode")
    private String focusLimitSelectMode = "Auto";

    @JsonProperty("IRCorrection")
    private int irCorrection = 2;

    @JsonProperty("Mode")
    private int mode = 3;

    // Mode = 1 ->
    // Mode = 2 -> Automatico
    // Mode = 3 -> Semiautomatico
    // Mode = 4 -> Manual

    @JsonProperty("Sensitivity")
    private int sensitivity = 0;

}
