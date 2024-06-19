package dev.hbm.qris_testingtools.Core.Function.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface FunctionProcessor {
    Object run (ObjectNode args);
}
