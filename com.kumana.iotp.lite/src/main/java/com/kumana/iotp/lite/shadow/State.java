package com.kumana.iotp.lite.shadow;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class State {

    private Reported reported;
    private Desired desired;

}
