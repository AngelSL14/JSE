package us.gonet.nabhi.jse.core.memory.task;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.jse.core.memory.style.BankSearch;
import us.gonet.nabhi.misc.jdb.entity.BankStyleEntity;
import us.gonet.nabhi.misc.jdb.repository.BankStyleRepository;
import us.gonet.nabhi.misc.model.jdbc.jdb.BankStyle;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class UpdateBankTask {

    private final Timer timer;
    private BankSearch bankSearch;
    private BankStyleRepository bankStyleRepository;
    private ModelMapper mapper;
    private int second = 15;
    private Type listType = new TypeToken < List < BankStyle > >() {
    }.getType();
    private static final Logger LOG = LoggerFactory.getLogger( UpdateBankTask.class );

    private final TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if ( second == 0 ) {
                try {
                    List < BankStyleEntity > entities = bankStyleRepository.findAll();
                    List < BankStyle > styles = mapper.map( entities, listType );
                    bankSearch.buildTable( styles );
                } catch ( ClassCastException e ) {
                    LOG.error( "Unable to retrieved list of Banks" );
                }
                second = 15;
            } else {
                second--;
            }
        }
    };

    @Autowired
    public UpdateBankTask( BankStyleRepository bankStyleRepository, BankSearch bankSearch, ModelMapper mapper ) {
        this.bankStyleRepository = bankStyleRepository;
        this.bankSearch = bankSearch;
        this.mapper = mapper;
        this.timer = new Timer( "UpdateBankTask" );
    }

    public void start() {
        timer.schedule( task, 0, 1000 );
    }

}
