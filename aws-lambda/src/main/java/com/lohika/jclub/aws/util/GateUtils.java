package com.lohika.jclub.aws.util;

import org.apache.commons.lang.StringUtils;

public class GateUtils {
    public static boolean isValid(String gateId) {
        return StringUtils.isNotBlank(gateId) && gateId.matches("(IN|OUT)-[\\d]{1,2}");
    }
}
