package com.lohika.jclub.aws.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IncomingEventDb {
    private UUID eventId;
    private String gateId;
    private String vrn;
    private long timestamp;
}
