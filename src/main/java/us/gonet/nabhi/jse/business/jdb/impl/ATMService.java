package us.gonet.nabhi.jse.business.jdb.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.gonet.nabhi.jse.business.jdb.IATMService;
import us.gonet.nabhi.jse.core.jdb.Repository;
import us.gonet.nabhi.jse.core.jdb.SanitizeModel;
import us.gonet.nabhi.jse.utils.StreamFilter;
import us.gonet.nabhi.misc.exception.*;
import us.gonet.nabhi.misc.jdb.entity.ATDEntity;
import us.gonet.nabhi.misc.jdb.entity.IDFEntity;
import us.gonet.nabhi.misc.jdb.entity.JournalEntity;
import us.gonet.nabhi.misc.jdb.entity.ScreenEntity;
import us.gonet.nabhi.misc.jdb.entity.personalized.ATDEntityCompose;
import us.gonet.nabhi.misc.jdb.entity.personalized.bin.PIDFEntity;
import us.gonet.nabhi.misc.jdb.entity.personalized.up.ATDUpTimeEntity;
import us.gonet.nabhi.misc.jdb.entity.personalized.up.IDFUpTimeEntity;
import us.gonet.nabhi.misc.model.JournalQuery;
import us.gonet.nabhi.misc.model.jdbc.jdb.ATD;
import us.gonet.nabhi.misc.model.jdbc.jdb.ButtonMapping;
import us.gonet.nabhi.misc.model.jdbc.jdb.Journal;
import us.gonet.nabhi.misc.model.jdbc.jdb.Screen;
import us.gonet.nabhi.misc.model.jdbc.jdb.up.ATDUp;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ATMService implements IATMService {

    private static final String JDB_02 = "JDB-02";
    private ModelMapper mapper;
    private StreamFilter < ATD > filterAtd;
    private SanitizeModel sanitizeModel;
    private Repository repository;

    @Autowired
    public ATMService( Repository repository, ModelMapper mapper, StreamFilter < ATD > filterAtd, SanitizeModel sanitizeModel ) {
        this.repository = repository;
        this.mapper = mapper;
        this.filterAtd = filterAtd;
        this.sanitizeModel = sanitizeModel;
    }

    @Override
    public ResponseWrapper < ATD > findATM( String terminalId ) {
        ResponseWrapper < ATD > wrapper = new ResponseWrapper <>();
        Optional < ATDEntity > oe;
        try {
            oe = repository.getAtdRepository().findById( filterAtd.sanitizeString( terminalId ) );
        } catch ( ServerException e ) {
            return filterAtd.sanitizeError();
        }
        if ( oe.isPresent() ) {
            wrapper.setCode( "00" );
            wrapper.addBody( mapper.map( oe.get(), ATD.class ) );
        } else {
            wrapper.setCode( "01" );
            wrapper.addError( new ErrorWS( JDB_02, "Invalid ID" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < ATD > findATM( String terminalId, String fiid ) {
        ResponseWrapper < ATD > wrapper = new ResponseWrapper <>();
        Optional < ATDEntity > oe;
        try {
            filterAtd.sanitizeString( fiid );
            filterAtd.sanitizeString( terminalId );
            IDFEntity entity = new IDFEntity();
            entity.setFiid( fiid );
            oe = repository.getAtdRepository().findByTerminalIdAndIdfEquals( terminalId, entity );
        } catch ( ServerException e ) {
            return filterAtd.sanitizeError();
        }
        if ( oe.isPresent() ) {
            wrapper.setCode( "00" );
            wrapper.addBody( mapper.map( oe.get(), ATD.class ) );
        } else {
            wrapper.setCode( "01" );
            wrapper.addError( new ErrorWS( JDB_02, "Invalid ID by Fiid" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < ATDUp > findATMUpTime( String terminalId, String fiid ) {
        ResponseWrapper < ATDUp > wrapper = new ResponseWrapper <>();
        Optional < ATDUpTimeEntity > oe = Optional.empty();
        try {
            filterAtd.sanitizeString( fiid );
            filterAtd.sanitizeString( terminalId );
            IDFUpTimeEntity entity = new IDFUpTimeEntity();
            entity.setFiid( fiid );
            oe = repository.getAtdUpTimeRepository().findByTerminalIdAndIdfEquals( terminalId, entity );
        } catch ( ServerException e ) {
            addErrors( e, wrapper );
        }
        if ( oe.isPresent() ) {
            wrapper.setCode( "00" );
            wrapper.addBody( mapper.map( oe.get(), ATDUp.class ) );
        } else {
            wrapper.setCode( "01" );
            wrapper.addError( new ErrorWS( JDB_02, "Invalid ID by Fiid" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < List < ATD > > findAll() {
        ResponseWrapper < List < ATD > > wrapper = new ResponseWrapper <>();
        List < ATDEntity > entities = repository.getAtdRepository().findAll();
        if ( !entities.isEmpty() ) {
            wrapper.setCode( "00" );
            Type listType = new TypeToken < List < ATD > >() {
            }.getType();
            wrapper.addBody( mapper.map( entities, listType ) );
        } else {
            wrapper.setCode( "01" );
            wrapper.addError( new ErrorWS( JDB_02, "List of ATMS empty" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < List < ATD > > findAll( String fiid ) {
        ResponseWrapper < List < ATD > > wrapper = new ResponseWrapper <>();
        try {
            filterAtd.sanitizeString( fiid );
        } catch ( ServerException e ) {
            addErrors( e, wrapper );
        }
        IDFEntity entity = new IDFEntity();
        entity.setFiid( fiid );
        List < ATDEntity > entities = repository.getAtdRepository().findAllByIdfEquals( entity );
        if ( !entities.isEmpty() ) {
            wrapper.setCode( "00" );
            Type listType = new TypeToken < List < ATD > >() {
            }.getType();
            wrapper.addBody( mapper.map( entities, listType ) );
        } else {
            wrapper.setCode( "01" );
            wrapper.addError( new ErrorWS( JDB_02, "List of ATMS empty by Fiid" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < List < ATDUp > > findAllUpTime( String fiid ) {
        ResponseWrapper < List < ATDUp > > wrapper = new ResponseWrapper <>();
        try {
            filterAtd.sanitizeString( fiid );
        } catch ( ServerException e ) {
            addErrors( e, wrapper );
        }
        IDFUpTimeEntity entity = new IDFUpTimeEntity();
        entity.setFiid( fiid );
        List < ATDUpTimeEntity > entities = repository.getAtdUpTimeRepository().findAllByIdfEquals( entity );
        if ( !entities.isEmpty() ) {
            wrapper.setCode( "00" );
            Type listType = new TypeToken < List < ATDUp > >() {
            }.getType();
            wrapper.addBody( mapper.map( entities, listType ) );
        } else {
            wrapper.setCode( "01" );
            wrapper.addError( new ErrorWS( JDB_02, "List of ATMS Up Time empty by Fiid" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < String > saveJournal( Journal journal ) {
        ResponseWrapper < String > wrapper = new ResponseWrapper <>();
        try {
            sanitizeModel.sanitize( journal );
        } catch ( SanitazeException e ) {
            addErrors( e, wrapper );
        }
        journal.setWriteDate( new Timestamp( new Date().getTime() ) );
        JournalEntity entity = mapper.map( journal, JournalEntity.class );
        repository.getJournalRepository().save( entity );
        wrapper.setCode( "00" );
        wrapper.addBody( "Add Journal Ok" );
        return wrapper;
    }

    @Override
    public ResponseWrapper < String > saveJournal( List < Journal > journals ) {
        ResponseWrapper < String > wrapper = new ResponseWrapper <>();
        try {
            for ( Journal j : journals ) {
                sanitizeModel.sanitize( j );
            }
        } catch ( SanitazeException e ) {
            addErrors( e, wrapper );
        }
        for ( Journal j : journals ) {
            j.setWriteDate( new Timestamp( new Date().getTime() ) );
        }
        Type listType = new TypeToken < List < JournalEntity > >() {
        }.getType();
        List < JournalEntity > entities = mapper.map( journals, listType );
        repository.getJournalRepository().saveAll( entities );
        wrapper.setCode( "00" );
        wrapper.addBody( "Add Journals Ok" );
        return wrapper;
    }

    @Override
    public ResponseWrapper < Screen > saveScreen( Screen screen ) {
        ResponseWrapper < Screen > wrapper = new ResponseWrapper <>();
        try {
            sanitizeModel.sanitize( screen );
        } catch ( SanitazeException e ) {
            addErrors( e, wrapper );
        }
        try {
            ScreenEntity entity = repository.getScreenRepository().save( mapper.map( screen, ScreenEntity.class ) );
            wrapper.setCode( "00" );
            wrapper.addBody( mapper.map( entity, Screen.class ) );
        } catch ( ClassCastException e ) {
            wrapper.setCode( "01" );
            wrapper.addError( new ErrorWS( "JDB-03", "Screen not exits" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < String > saveSingleScreen( String terminalId, ButtonMapping single ) {
        ResponseWrapper < String > wrapper = new ResponseWrapper <>();
        try {
            terminalId = filterAtd.sanitizeString( terminalId );
            sanitizeModel.sanitize( single );
        } catch ( ServerException | SanitazeException e ) {
            addErrors( e, wrapper );
        }
        Optional < ScreenEntity > oe = repository.getScreenRepository().findById( terminalId );
        if ( oe.isPresent() ) {
            boolean isNew = true;
            ScreenEntity entity = oe.get();
            wrapper.setCode( "00" );
            /*TODO
            for ( ButtonMapping b : entity.getButtonsAllowed().getButtons() ) {
                if ( b.getId() == single.getId() ) {
                    b.setBitmap( single.getBitmap() );
                    b.setScreenComponent( single.getScreenComponent() );
                    b.setLayout( single.getLayout() );
                    isNew = false;
                    repository.getScreenRepository().save( entity );
                    wrapper.addBody( "Add OK" );
                    break;
                }
            }*/
            if ( isNew ) {
                //TODO entity.getButtonsAllowed().getButtons().add( single );
                repository.getScreenRepository().save( entity );
                wrapper.addBody( "Update OK" );
            }
        } else {
            wrapper.setCode( "01" );
            wrapper.addError( new ErrorWS( "JDB-03", "Screen not exits" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < List < Journal > > find( String... id ) {
        ResponseWrapper < List < Journal > > wrapper = new ResponseWrapper <>();
        try {
            for ( String d : id ) {
                filterAtd.sanitizeString( d );
            }
        } catch ( ServerException e ) {
            addErrors( e, wrapper );
        }
        Type listType = new TypeToken < List < Journal > >() {
        }.getType();
        if ( id.length == 1 ) {
            List < JournalEntity > entities = repository.getJournalRepository().findByTerminalId( id[ 0 ] );
            wrapper.addBody( mapper.map( entities, listType ) );
        } else {
            PIDFEntity idf = new PIDFEntity();
            idf.setFiid( id[ 1 ] );
            ATDEntityCompose atd = repository.getAtdRepositoryCompose().findByTerminalIdAndIdfEquals( id[ 0 ], idf );
            if ( atd == null ) {
                wrapper.setCode( "96" );
                wrapper.addError( new ErrorWS( "JDB-04", "Records no exists" ) );
                return wrapper;
            }
            wrapper.addBody( mapper.map( atd.getJorunal(), listType ) );
        }
        wrapper.setCode( "00" );
        return wrapper;
    }

    @Override
    public ResponseWrapper < List < Journal > > findByQuery( JournalQuery query, String... id ) {
        ResponseWrapper < List < Journal > > wrapper = new ResponseWrapper <>();
        try {
            filterAtd.sanitizeString( query.getTerminalId() );
            if ( id != null ) {
                for ( String d : id ) {
                    filterAtd.sanitizeString( d );
                }
            }
        } catch ( ServerException e ) {
            addErrors( e, wrapper );
        }
        Type listType = new TypeToken < List < Journal > >() {
        }.getType();
        List < JournalEntity > entities;
        if ( id == null ) {
            entities = repository.getJournalRepository().findByTerminalIdAndWriteDateBetween( query.getTerminalId(), query.getFrom(), query.getTo() );
            wrapper.addBody( mapper.map( entities, listType ) );
        } else {
            PIDFEntity idf = new PIDFEntity();
            idf.setFiid( id[ 0 ] );
            ATDEntityCompose atd = repository.getAtdRepositoryCompose().findByTerminalIdAndIdfEquals( query.getTerminalId(), idf );
            if ( atd != null ) {
                entities = repository.getJournalRepository().findByTerminalIdAndWriteDateBetween( query.getTerminalId(), query.getFrom(), query.getTo() );
                wrapper.addBody( mapper.map( entities, listType ) );
            } else {
                wrapper.setCode( "96" );
                wrapper.addError( new ErrorWS( "JDB-04", "Records no exists" ) );
                return wrapper;
            }
        }
        wrapper.setCode( "00" );
        return wrapper;
    }

    private void addErrors( IException e, ResponseWrapper wrapper ) {
        wrapper.setCode( "96" );
        wrapper.addAllError( e.getErrors() );
    }
}
