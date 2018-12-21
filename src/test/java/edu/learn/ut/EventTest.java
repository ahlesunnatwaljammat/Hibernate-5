package edu.learn.ut;


import edu.learn.config.DBFactory;
import edu.learn.entities.Event;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;

public class EventTest {
    private static DBFactory dbFactory;
    private static String HOSTNAME;

    @BeforeAll
    public static void init() {
        dbFactory = DBFactory.INSTANCE;
        try {
            HOSTNAME = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void destroy() {
        dbFactory.shutdownSessionFactory();
    }

    @Test()
    public void pass_001() {
        DBFactory dbFactory = DBFactory.INSTANCE;
        Session session = dbFactory.getSession();

        Event event = new Event();
        event.setEventName("Meeting 609" + HOSTNAME);
        event.setDate(Instant.now());
        Transaction transaction = session.beginTransaction();
        long saved = (long) session.save(event);
        transaction.commit();
        session.close();

        Assertions.assertTrue(saved > 0);
    }

    @Test()
    public void pass_002() {
        DBFactory dbFactory = DBFactory.INSTANCE;
        Session session = dbFactory.getSession();
        Query<Event> eventQuery = session.createQuery("from Event e where e.eventName=:eventName", Event.class);
        eventQuery.setParameter("eventName", "Meeting 609" + HOSTNAME);

        Event event = eventQuery.getSingleResult();
        session.close();

        Assertions.assertTrue(event != null);
    }

    @Test()
    public void pass_003() {
        DBFactory dbFactory = DBFactory.INSTANCE;
        Session session = dbFactory.getSession();
        Query<Event> eventQuery = session.createQuery("from Event e where e.eventName=:eventName", Event.class);
        eventQuery.setParameter("eventName", "Meeting 609" + HOSTNAME);

        Event event = eventQuery.getSingleResult();
        Transaction transaction = session.beginTransaction();
        event.setEventName("Meeting 609-A" + HOSTNAME);
        session.save(event);
        transaction.commit();

        eventQuery = session.createQuery("from Event e where e.eventName=:eventName", Event.class);
        eventQuery.setParameter("eventName", "Meeting 609-A" + HOSTNAME);
        event = eventQuery.getSingleResult();

        session.close();

        Assertions.assertEquals("Meeting 609-A" + HOSTNAME,event.getEventName());
    }

    @Test()
    public void pass_004() {
        DBFactory dbFactory = DBFactory.INSTANCE;
        Session session = dbFactory.getSession();

        Query<Event> eventQuery = session.createQuery("from Event e where e.eventName=:eventName", Event.class);
        eventQuery.setParameter("eventName", "Meeting 609-A" + HOSTNAME);
        Event event = eventQuery.getSingleResult();

        Transaction transaction = session.beginTransaction();
        session.delete(event);
        transaction.commit();
        session.close();
    }
}
