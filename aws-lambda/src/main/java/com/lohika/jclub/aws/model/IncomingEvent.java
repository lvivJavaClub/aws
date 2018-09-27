package com.lohika.jclub.aws.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IncomingEvent {
    private String gateId;
    private String vrn;
    private long timestamp;
}
