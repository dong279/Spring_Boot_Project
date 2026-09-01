package com.rookies6.myspringbootlab.environment;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class MyEnvironment {
    String mode;
}
