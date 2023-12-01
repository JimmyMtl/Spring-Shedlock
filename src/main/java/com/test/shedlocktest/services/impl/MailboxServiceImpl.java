package com.test.shedlocktest.services.impl;

import com.test.shedlocktest.exceptions.StoreException;
import com.test.shedlocktest.services.MailboxService;
import jakarta.mail.*;
import jakarta.mail.event.MessageCountAdapter;
import jakarta.mail.event.MessageCountEvent;
import org.eclipse.angus.mail.imap.IMAPFolder;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Slf4j
@Service
public class MailboxServiceImpl implements MailboxService {
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private String port;

    private Properties initProperties() {
        Properties properties = new Properties();
        properties.put("mail.imap.host", this.host);
        properties.put("mail.imap.port", this.port);
        properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imap.usesocketchannels", "true");
        return properties;
    }

    private Session initConnection(Properties properties) {
        return Session.getDefaultInstance(properties, null);
    }

    private Store initStore(Session session) throws StoreException {
        try {
            Store store = session.getStore("imap");
            store.connect(this.host, Integer.parseInt(this.port), this.username, this.password);
            return store;
        } catch (Exception e) {
            log.error("[ERROR: '{}']", e.getMessage());
            throw new StoreException("[STORE ERROR: " + e.getMessage() + "]", e);
        }
    }

    private void initListener(Folder folder, int frequency, int runForInSecondes) throws MessagingException {
        boolean supportsIdle = false;
        try {
            if (folder instanceof IMAPFolder) {
                IMAPFolder f = (IMAPFolder) folder;
                f.idle();
                supportsIdle = true;
            }
        } catch (FolderClosedException fex) {
            throw fex;
        } catch (MessagingException mex) {
            supportsIdle = false;
        }
        int waitCount = 0;
        for (; ; ) {
            if (waitCount >= runForInSecondes) {
                log.info("Close IDLE connection by timeout '{}' minutes or '{}' seconds", runForInSecondes, waitCount);
                break;
            }
            if (supportsIdle && folder instanceof IMAPFolder) {
                IMAPFolder f = (IMAPFolder) folder;
                f.idle();
                log.info("IDLE done");
            } else {
                try {
                    Thread.sleep(frequency);
                    folder.getMessageCount();
                    waitCount++;
                } catch (InterruptedException e) {
                    log.error("[ERROR: '{}']", e.getMessage());
                }
            }
        }
        log.info("initListener() finished");
    }

    private Folder initMessageCountListener(Folder folder) {

        folder.addMessageCountListener(new MessageCountAdapter() {
            @Override
            public void messagesAdded(MessageCountEvent e) {
                try {
                    for (Message message : e.getMessages()) {
                        log.info("messagesAdded() message: {}", message.getSubject());
                    }
                } catch (Exception ex) {
                    log.error("[ERROR: '{}']", ex.getMessage());
                }
            }
        });
        return folder;
    }

    private void initMailListenerProcess(Store store, int runForInSecondes) {
        try {
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            log.info("initMailListenerProcess() inbox: {}", inbox);
            inbox = this.initMessageCountListener(inbox);
            log.info("initListenerProcess() inbox: {}", inbox);
            this.initListener(inbox, 1000, runForInSecondes);
            log.info("Close connection");
            store.close();
        } catch (MessagingException e) {
            log.error("mailProcessing() failed: {}", e.getMessage());
        }
    }

    public void run(int runForInSecondes) {
        log.info("run() started");
        Properties properties = this.initProperties();
        Session session = null;
        session = this.initConnection(properties);
        try (Store store = this.initStore(session)) {
            log.info("run() store: {}", store);
            this.initMailListenerProcess(store, runForInSecondes);

        } catch (MessagingException | StoreException e) {
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session = null;
            }
        }
    }
}
