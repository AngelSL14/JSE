package us.gonet.nabhi.jse.core.memory.task;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.jse.core.memory.atm.ATMSearch;
import us.gonet.nabhi.misc.jdb.entity.ATDEntity;
import us.gonet.nabhi.misc.jdb.repository.ATDRepository;
import us.gonet.nabhi.misc.model.jdbc.jdb.ATD;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class UpdateATMTask {

    private final Timer timer;
    private ATMSearch atmSearch;
    private ATDRepository atdRepository;
    private ModelMapper mapper;
    private Type listType = new TypeToken < List < ATD > >() {
    }.getType();
    private int second = 60;
    private static final Logger LOG = LoggerFactory.getLogger( UpdateATMTask.class );

    private final TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if ( second == 0 ) {
                try {
                    List < ATDEntity > entities = atdRepository.findAll();
                    List < ATD > atds = mapper.map( entities, listType );
                    atmSearch.updateATMTable( atds );
                } catch ( ClassCastException e ) {
                    LOG.error( "Unable to retrieved list of ATM" );
                }
                second = 60;
            } else {
                second--;
            }
        }
    };

    @Autowired
    public UpdateATMTask( ATDRepository atdRepository, ATMSearch atmSearch, ModelMapper mapper ) {
        this.atdRepository = atdRepository;
        this.atmSearch = atmSearch;
        this.mapper = mapper;
        this.timer = new Timer( "UpdateATMTask" );
    }

    public void start() {
        timer.schedule( task, 0, 1000 );
    }

}
