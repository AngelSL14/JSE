package us.gonet.nabhi.jse.business.impl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.jse.business.IAtmNotifications;
import us.gonet.nabhi.jse.core.jdb.Repository;
import us.gonet.nabhi.jse.i8583.reversal.Reversal;
import us.gonet.nabhi.jse.journal.JournalWriter;
import us.gonet.nabhi.jse.notification.SaveNotification;
import us.gonet.nabhi.misc.jdb.entity.DevicesEntity;
import us.gonet.nabhi.misc.jdb.repository.DeviceRepository;
import us.gonet.nabhi.misc.model.devices.DevicesWrapper;
import us.gonet.nabhi.misc.model.devices.cdm.Cassette;
import us.gonet.nabhi.misc.model.devices.cdm.Dispenser;
import us.gonet.nabhi.misc.model.devices.constants.AtmEvent;
import us.gonet.nabhi.misc.model.devices.constants.AtmResponse;
import us.gonet.nabhi.misc.model.devices.constants.cdm.CDMEvent;
import us.gonet.nabhi.misc.model.devices.constants.cdm.CDMResponse;
import us.gonet.nabhi.misc.model.devices.constants.idc.IDCEvent;
import us.gonet.nabhi.misc.model.devices.constants.pin.PINEvent;
import us.gonet.nabhi.misc.model.devices.constants.ptr.PTREvent;
import us.gonet.nabhi.misc.model.devices.idc.Reader;
import us.gonet.nabhi.misc.model.devices.pin.EPP;
import us.gonet.nabhi.misc.model.devices.ptr.Printer;
import us.gonet.nabhi.misc.model.jse.request.AtmNotificationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AtmNotificationsService implements IAtmNotifications {

    private static final Logger LOG = LoggerFactory.getLogger( AtmNotificationsService.class );

    private Reversal reversalPartial;
    private JournalWriter journalWriter;
    private SaveNotification saveNotification;
    private Repository repository;
    private ModelMapper modelMapper;


    @Autowired
    public AtmNotificationsService( Reversal reversalPartial, JournalWriter journalWriter, SaveNotification saveNotification, Repository repository ) {
        this.reversalPartial = reversalPartial;
        this.journalWriter = journalWriter;
        this.saveNotification = saveNotification;
        this.repository = repository;
        modelMapper = new ModelMapper();
    }

    @Override
    public void sendToDevice( AtmNotificationModel model ) {
        int code = ( int ) model.getExtra().get( "msg1" );
        switch ( model.getDevice() ) {
            case "IDC":
                IDCEvent idcEvent = IDCEvent.getEventFromCode( code );
                checkAction( model, idcEvent );
                break;
            case "PTR":
                PTREvent ptrEvent = PTREvent.getEventFromCode( code );
                checkAction( model, ptrEvent );
                break;
            case "CDM":
                CDMEvent cdmEvent = CDMEvent.getEventFromCode( code );
                AtmResponse response = CDMResponse.getResponseByCode( code );
                checkAction( model, cdmEvent );
                cdmEvent( model, cdmEvent );
                cdmResponse( model, response );
                break;
            case "PIN":
                PINEvent pinEvent = PINEvent.getEventFromCode( code );
                checkAction( model, pinEvent );
                break;
            case "SIU":
                break;
            default:
                break;
        }
    }

    private void cdmEvent( AtmNotificationModel model, AtmEvent event ) {
        if ( event.getActions().substring( 3, 4 ).equals( "1" ) ) {
            action( model );
        }
    }

    private void cdmResponse( AtmNotificationModel model, AtmResponse event ) {
        if ( event.getSeverity().equals( "-1" ) || event.getSeverity().equals( "-2" ) ) {
            LOG.info(model.getTermId() + ": " + event.getMessage());
            action( model );
        }
    }

    private void action( AtmNotificationModel model ) {
        final String code = model.getExtra().get( "msg1" ).toString();
        List < Cassette > cassettesInput = new ArrayList <>();
        if ( code.equals( "300" ) || code.contains( "-" ) ) {
            List maps = modelMapper.map( model.getExtra().get( "msg3" ), List.class );
            for ( Object cassettes : maps ) {
                cassettesInput.add( modelMapper.map( cassettes, Cassette.class ) );
            }
        } else {
            Cassette cashUnitCounters = modelMapper.map( model.getExtra().get( "msg3" ), Cassette.class );
            cassettesInput.add( cashUnitCounters );
        }
        findAndUpdateCashUnits( cassettesInput, model.getTermId() );

        if ( code.contains( "-" ) ) {
            List < Cassette > cassettesBefore = new ArrayList <>();
            List maps = modelMapper.map( model.getExtra().get( "msg4" ), List.class );
            for ( Object cassettes : maps ) {
                cassettesBefore.add( modelMapper.map( cassettes, Cassette.class ) );
            }
            LOG.error("Generando reverso");
            doReversal( cassettesInput, cassettesBefore, model );
        }

    }

    private void checkAction( AtmNotificationModel model, AtmEvent event ) {
        String actions = event.getActions();
        if ( actions.substring( 0, 1 ).equals( "1" ) ) {
            journalWriter.writeJournal( model.getTermId(), event.getJournalMessage() );
        }
        if ( actions.substring( 1, 2 ).equals( "1" ) ) {
            // TODO Find and update device depending of name and status
        }
        if ( actions.substring( 2, 3 ).equals( "1" ) ) {
            saveNotification.saveDeviceNotif( model, event.getJournalMessage() );
        }

    }

    //TODO
    @Override
    public boolean updateDevice( AtmNotificationModel model ) {
        Optional < DevicesEntity > optional = repository.getDeviceRepository().findById( model.getTermId() );
        if ( optional.isPresent() ) {
            DevicesEntity entity = optional.get();
            DevicesWrapper devicesWrapper = entity.getTerminalDevices();
            switch ( model.getDevice() ) {
                case "IDC":
                    Reader reader = modelMapper.map( model.getExtra().get( "msg3" ), Reader.class );
                    devicesWrapper.setCardReader(reader);
                    break;
                case "CDM":
                    Dispenser dispenser = modelMapper.map( model.getExtra().get( "msg3" ), Dispenser.class );
                    devicesWrapper.setDispenser( dispenser );
                    break;
                case "PTR":
                    Printer printer = modelMapper.map( model.getExtra().get( "msg3" ), Printer.class );
                    devicesWrapper.setPrinter( printer );
                    break;
                case "PIN":
                    EPP pinpad = modelMapper.map( model.getExtra().get( "msg3" ), EPP.class );
                    devicesWrapper.setPinPad( pinpad );
                    break;
                default:
                    LOG.error( "Device not found" );
                    break;
            }
            entity.setTerminalDevices( devicesWrapper );
            repository.getDeviceRepository().save( entity );
        }

        return false;
    }

    private void findAndUpdateCashUnits( List < Cassette > cassettesInput, String termID ) {
        //GET DEVICES
        Optional < DevicesEntity > optional = repository.getDeviceRepository().findById( termID );
        if ( optional.isPresent() ) {
            DevicesEntity devicesEntity = optional.get();
            // GET DEVICE WRAPPER
            DevicesWrapper deviceWrapper = devicesEntity.getTerminalDevices();
            //GET DISPENSER
            Dispenser dispenser = deviceWrapper.getDispenser();
            // GET LIST OF CASSETTES
            List < Cassette > cassettesBD = dispenser.getCassettes();
            //REPLACE CASSETTES
            for ( Cassette cassetteInput : cassettesInput ) {
                for ( int j = 0; j < cassettesBD.size(); j++ ) {
                    if ( cassetteInput.getCassetteIndex() == cassettesBD.get( j ).getCassetteIndex() ) {
                        cassettesBD.set( j, cassetteInput );
                    }
                }
            }
            dispenser.setCassettes( cassettesBD );
            deviceWrapper.setDispenser( dispenser );
            devicesEntity.setTerminalDevices( deviceWrapper );
            repository.getDeviceRepository().save( devicesEntity );
        }

    }

    private int compareCounters( List < Cassette > before, List < Cassette > after ) {
        List < Cassette > resultado = new ArrayList <>();
        for ( Cassette cassetteInput : after ) {
            for ( Cassette value : before ) {
                if ( cassetteInput.getCassetteIndex() == value.getCassetteIndex() ) {
                    Cassette cassette = new Cassette();
                    cassette.setDenomination( cassetteInput.getDenomination() );
                    cassette.setPresented( cassetteInput.getPresented() - value.getPresented() );
                    resultado.add( cassette );
                }
            }
        }
        int total = 0;
        for ( Cassette cassette : resultado ) {
            total += cassette.getDenomination() * cassette.getPresented();
        }
        return total;
    }

    private void doReversal( List < Cassette > input, List < Cassette > bd, AtmNotificationModel model ) {
        reversalPartial.makeReversal( model, compareCounters( input, bd ) );
        model.getExtra().remove( "msg3" );
        model.getExtra().remove( "msg4" );
        journalWriter.writeDeviceNotify( model.getTermId(), model.getExtra().get( "msg2" ).toString() );

    }
}


