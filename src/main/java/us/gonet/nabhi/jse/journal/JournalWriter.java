package us.gonet.nabhi.jse.journal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.jse.utils.Utils;
import us.gonet.nabhi.misc.jdb.entity.JournalEntity;
import us.gonet.nabhi.misc.jdb.repository.JournalRepository;

import java.sql.Timestamp;
import java.util.Date;

@Component
public class JournalWriter {

    private static final Logger LOG = LoggerFactory.getLogger( JournalWriter.class );
    private JournalRepository journalRepository;

    @Autowired
    public JournalWriter( JournalRepository journalRepository ) {
        this.journalRepository = journalRepository;
    }

    public void writeJournal( String atm, String message ) {
        atm = Utils.sanitize( atm );
        message = Utils.sanitize( message );
        JournalEntity entity = new JournalEntity();
        entity.setTerminalId( atm );
        entity.setTerminalIdJournal( atm );
        entity.setMessage( message );
        entity.setWriteDate( new Timestamp( new Date().getTime() ) );
        journalRepository.save( entity );
    }

    //TODO Revisar notificaciones
    public void writeDeviceNotify( String atm, String message ) {
        atm = Utils.sanitize( atm );
        message = Utils.sanitize( message );
        //try {
        //    jdbRequester.writeJournal( up, message, "/deviceNotif/write/{up}" );
        //} catch ( ServerException e ) {
        //    LOG.error( "Error en la peticion entre servicios" );

        //}
    }


}
