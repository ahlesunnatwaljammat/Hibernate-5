package edu.learn.ut;

import edu.learn.config.DBFactory;
import edu.learn.entities.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;

public class UserTest {
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
        dbFactory.shutdownEntityManagerFactory();
    }

    @Test
    public void pass_001(){
        User user = new User();
        user.setUsername("nabbasi" + HOSTNAME);
        user.setPassword("x");
        user.setDate(Instant.now());

        EntityManager entityManager = dbFactory.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(user);
        transaction.commit();
    }

    @Test
    public void pass_002(){
        EntityManager entityManager = dbFactory.getEntityManager();
        TypedQuery<User> findUser = entityManager.createQuery("from User where username=:username", User.class);
        findUser.setParameter("username", "nabbasi" + HOSTNAME);
        User user = findUser.getSingleResult();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        user.setPassword("xxx");
        entityManager.merge(user);
        transaction.commit();
    }

    @Test
    public void pass_003(){
        EntityManager entityManager = dbFactory.getEntityManager();
        TypedQuery<User> findUser = entityManager.createQuery("from User where username=:username", User.class);
        findUser.setParameter("username", "nabbasi" + HOSTNAME);
        User user = findUser.getSingleResult();

        Assertions.assertEquals("xxx", user.getPassword());
    }

    @Test
    public void pass_009(){
        EntityManager entityManager = dbFactory.getEntityManager();
        TypedQuery<User> findUser = entityManager.createQuery("from User where username=:username", User.class);
        findUser.setParameter("username", "nabbasi" + HOSTNAME);
        User user = findUser.getSingleResult();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.remove(user);
        transaction.commit();
    }
}
