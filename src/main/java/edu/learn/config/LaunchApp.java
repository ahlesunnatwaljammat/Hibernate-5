package edu.learn.config;

import edu.learn.entities.Event;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import java.time.Instant;

@Slf4j
public class LaunchApp {
    public static void main(String[] args) {
        log.info("===================================");
        DBFactory factory = DBFactory.INSTANCE;
        Session session = factory.getSession();

        Event event = new Event();
        event.setEvent("Meeting 609");
        event.setDate(Instant.now());
        Transaction transaction = session.beginTransaction();
        session.save(event);
        transaction.commit();


        session.createQuery("from Event", Event.class).stream().forEach(System.out::println);
        session.close();
        log.info("===================================");
        EntityManager entityManager = factory.getEntityManager();
        entityManager.createQuery("from Event", Event.class).getResultList().stream().forEach(System.out::println);
    }
}
