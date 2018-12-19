package edu.learn.config;

import edu.learn.entities.Event;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public enum DBFactory {
    INSTANCE;

    private static final Lock LOCK = new ReentrantLock(true);
    private static SessionFactory sessionFactory = null;


    @PersistenceUnit
    private EntityManagerFactory emf;

    @PersistenceContext
    private EntityManager em;



    private SessionFactory getSessionFactory() {

        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure( "/hibernate.cfg.xml" )
                .build();

        Metadata metadata = new MetadataSources( standardRegistry )
                .addAnnotatedClass( Event.class )
                //.addPackage("edu.learn.entities")
                /*.addAnnotatedClassName( "org.hibernate.example.Customer" )
                .addResource( "org/hibernate/example/Order.hbm.xml" )
                .addResource( "org/hibernate/example/Product.orm.xml" )*/
                .getMetadataBuilder()
                .applyImplicitNamingStrategy( ImplicitNamingStrategyJpaCompliantImpl.INSTANCE )
                .build();

        SessionFactoryBuilder sessionFactoryBuilder = metadata.getSessionFactoryBuilder();

        // Supply a SessionFactory-level Interceptor
        //sessionFactoryBuilder.applyInterceptor( new CustomSessionFactoryInterceptor() );

        // Add a custom observer
        //sessionFactoryBuilder.addSessionFactoryObservers( new CustomSessionFactoryObserver() );

        // Apply a CDI BeanManager ( for JPA event listeners )
        //sessionFactoryBuilder.applyBeanManager( getBeanManager() );

        return sessionFactoryBuilder.build();
    }

    public Session getSession() {
        try {
            LOCK.lock();
            Session session = this.getSessionFactory().openSession();
            if(session.isOpen()){
                return session;
            } else {
                return sessionFactory.openSession();
            }
        }finally {
            LOCK.unlock();
        }
    }

    public EntityManager getEntityManager() {
        this.emf = Persistence.createEntityManagerFactory("TEST");
        this.em = this.emf.createEntityManager();
        return this.em;
    }

}
