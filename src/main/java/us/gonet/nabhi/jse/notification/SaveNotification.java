package us.gonet.nabhi.jse.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.jse.codesatm.AtmEvent;
import us.gonet.nabhi.jse.codesatm.AtmResponse;
import us.gonet.nabhi.jse.journal.JournalWriter;
import us.gonet.nabhi.misc.model.jse.request.AtmNotificationModel;

@Component
public class SaveNotification {

    private JournalWriter journalWriter;

    @Autowired
    public SaveNotification( JournalWriter journalWriter ) {
        this.journalWriter = journalWriter;
    }

    public void saveJournal( AtmNotificationModel model, AtmEvent atmEvent ) {
        journalWriter.writeJournal( model.getTermId(), atmEvent.getJournalMessage() );
    }

    public void saveJournalResponse( AtmNotificationModel model, AtmResponse atmEvent ) {
        journalWriter.writeJournal( model.getTermId(), atmEvent.getMessage() );
    }

    public void saveDeviceNotif( AtmNotificationModel model, String atmEvent ) {
        journalWriter.writeDeviceNotify( model.getTermId(), atmEvent );
    }
}
