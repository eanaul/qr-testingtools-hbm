package dev.hbm.qris_testingtools.Core.Server.Collection;

import com.sun.net.httpserver.HttpServer;

import java.util.LinkedHashMap;
import java.util.Map;

public class ServerCollection {
    public final static Map<Long, HttpServer> Server_Collection = new LinkedHashMap<>();
}
