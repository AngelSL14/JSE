package us.gonet.nabhi.jse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.jse.core.memory.MemoryTableBuilder;
import us.gonet.nabhi.jse.core.memory.task.UpdateATMTask;
import us.gonet.nabhi.jse.core.memory.task.UpdateBankTask;
import us.gonet.nabhi.jse.security.auth.JwtUtilATM;
import us.gonet.nabhi.misc.rest.header.HttpHeadersCustom;
import us.gonet.nabhi.misc.rest.iso.ISORequester;
import us.gonet.nabhi.misc.rest.jke.JKERequester;
import us.gonet.nabhi.misc.rest.security.auth.task.Endpoint;

@Component
public class Initializer {

    private JKERequester jkeRequester;
    private ISORequester isoRequester;
    private Endpoint endpoint;
    private HttpHeadersCustom httpHeadersCustom;
    private JwtUtilATM jwtUtilATM;
    private MemoryTableBuilder memoryTableBuilder;
    private UpdateATMTask updateATMTask;
    private UpdateBankTask updateBankTask;

    @Autowired
    public Initializer( JKERequester jkeRequester, ISORequester isoRequester, Endpoint endpoint, HttpHeadersCustom httpHeadersCustom, JwtUtilATM jwtUtilATM, MemoryTableBuilder memoryTableBuilder ) {
        this.jkeRequester = jkeRequester;
        this.isoRequester = isoRequester;
        this.endpoint = endpoint;
        this.httpHeadersCustom = httpHeadersCustom;
        this.jwtUtilATM = jwtUtilATM;
        this.memoryTableBuilder = memoryTableBuilder;
    }

    @Autowired
    public void setUpdateATMTask( UpdateATMTask updateATMTask ) {
        this.updateATMTask = updateATMTask;
    }

    @Autowired
    public void setUpdateBankTask( UpdateBankTask updateBankTask ) {
        this.updateBankTask = updateBankTask;
    }

    public void init() {
        jkeRequester.init( endpoint, httpHeadersCustom );
        isoRequester.init( endpoint, httpHeadersCustom );
        memoryTableBuilder.buildBinTable();
        memoryTableBuilder.buildIDFTable();
        memoryTableBuilder.buildBankTable();
        memoryTableBuilder.buildATMTable();
    }

    public void setATM() {
        updateATMTask.start();
        updateBankTask.start();
    }
}
