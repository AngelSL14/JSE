package us.gonet.nabhi.jse.core.jdb;

import org.springframework.stereotype.Component;
import us.gonet.nabhi.misc.jdb.entity.token.TokenUsersDetailEntity;
import us.gonet.nabhi.misc.jdb.entity.token.TokenUsersEntity;
import us.gonet.nabhi.misc.jdb.listener.IAuditListener;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

@Component
public class AuditListener implements IAuditListener {


    @PostPersist
    @PostUpdate
    @PostRemove
    @Override
    public void beforeAnyOperation( Object object ) {
        if ( object instanceof TokenUsersEntity ) {
        }
        if ( object instanceof TokenUsersDetailEntity ) {
        }
    }
}
