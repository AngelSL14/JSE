package us.gonet.nabhi.jse.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.jse.business.ICapabilitiesScreen;
import us.gonet.nabhi.jse.core.memory.MemoryTableBuilder;
import us.gonet.nabhi.jse.core.memory.atm.ATMSearch;
import us.gonet.nabhi.jse.core.memory.style.BankSearch;
import us.gonet.nabhi.misc.exception.ATMException;
import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.model.jdbc.jdb.ATD;
import us.gonet.nabhi.misc.model.jdbc.jdb.BankStyle;
import us.gonet.nabhi.misc.model.jdbc.jdb.ButtonMapping;
import us.gonet.nabhi.misc.model.jdbc.jdb.Screen;
import us.gonet.nabhi.misc.model.jse.Publicity;
import us.gonet.nabhi.misc.model.jse.request.Generic;
import us.gonet.nabhi.misc.model.jse.response.ScreenCapabilities;

import java.util.List;


@Component
public class CapabilitiesScreen implements ICapabilitiesScreen {

    private ATMSearch atmSearch;
    private BankSearch bankSearch;
    private MemoryTableBuilder memoryTableBuilder;

    @Autowired
    public CapabilitiesScreen( ATMSearch atmSearch, BankSearch bankSearch, MemoryTableBuilder memoryTableBuilder ) {
        this.atmSearch = atmSearch;
        this.bankSearch = bankSearch;
        this.memoryTableBuilder = memoryTableBuilder;
    }

    @Override
    public ResponseWrapper < ScreenCapabilities > getButtons( Generic generic ) {
        ResponseWrapper < ScreenCapabilities > wrapper = new ResponseWrapper <>();
        ATD atd;
        try {
            atd = atmSearch.searchByIP( generic.getIp() );
        } catch ( ATMException e ) {
            wrapper.setCode( "-500" );
            wrapper.addAllError( e.getErrors() );
            return wrapper;
        }
        List < ButtonMapping > buttonService = atd.getAtm().getScreen().getScreenGroup().getButtonsAllowed().getButtons();
        for ( ButtonMapping b : buttonService ) {
            ScreenCapabilities model = new ScreenCapabilities();
            model.setActiveFDKs( b.getBitmap() );
            model.setScreen( b.getScreenComponent() );
            wrapper.addBody( model );
        }
        wrapper.setCode( "200" );
        return wrapper;
    }

    @Override
    public ResponseWrapper < Publicity > getPublicity( Generic generic ) {
        ResponseWrapper < Publicity > wrapper = new ResponseWrapper <>();
        try {
            Screen screen = atmSearch.searchInDataBase( generic.getTermId() ).getAtm().getScreen();
            Publicity publicity = new Publicity();
            publicity.setScreenType( screen.getScreenType() );
            publicity.setNamePublicity( screen.getScreenGroup().getPublicityName() );
            BankStyle style = new BankStyle();
            style.setDashboard( screen.getScreenGroup().getBodyStyle() );
            style.setButtons( screen.getScreenGroup().getButtonsStyle() );
            style.setBackgroundImage( screen.getScreenGroup().getBackGround() );
            publicity.setBankStyle( style );
            wrapper.setCode( "200" );
            wrapper.addBody( publicity );
        } catch ( ATMException e ) {
            wrapper.setCode( "-500" );
            wrapper.addAllError( e.getErrors() );
            return wrapper;
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < Screen > getAllScreen( Generic generic ) {
        ResponseWrapper < Screen > wrapper = new ResponseWrapper <>();
        try {
            wrapper.addBody( atmSearch.searchInDataBase( generic.getTermId() ).getAtm().getScreen() );
            wrapper.setCode( "00" );
        } catch ( ATMException e ) {
            wrapper.setCode( "-500" );
            wrapper.addAllError( e.getErrors() );
            return wrapper;
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < Integer > ping( String ip ) {
        try {
            ATD atd = atmSearch.searchByIP( ip );

        } catch ( ATMException e ) {
            e.printStackTrace();
        }
        return new ResponseWrapper <>();
    }
}
