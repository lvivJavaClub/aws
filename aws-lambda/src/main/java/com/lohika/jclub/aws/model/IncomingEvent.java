package com.lohika.jclub.aws.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IncomingEvent {
    private String gateId;
    private String vrn;
    private long timestamp;
}
