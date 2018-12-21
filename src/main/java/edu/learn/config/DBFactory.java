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
    private static StandardServiceRegistry standardRegistry;
    private Session session;


    private static EntityManagerFactory emf;
    private EntityManager em;

    private static SessionFactory getSessionFactory() {

        if( sessionFactory == null ) {
            standardRegistry = new StandardServiceRegistryBuilder()
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

            sessionFactory = sessionFactoryBuilder.build();
            return sessionFactory;
        } else {
            return sessionFactory;
        }
    }

    public void shutdownSessionFactory() {
        if(sessionFactory.isOpen()){
            sessionFactory.close();
            StandardServiceRegistryBuilder.destroy(standardRegistry);
        }
    }

    public Session getSession() {
        try {
            LOCK.lock();

            if(null != this.session && this.session.isOpen()){
                return session;
            } else {
                this.session = this.getSessionFactory().openSession();
                return this.session;
            }
        }finally {
            LOCK.unlock();
        }
    }

    private static EntityManagerFactory getEntityManagerFactory() {
        try {
            LOCK.lock();

            if(emf == null){
                emf = Persistence.createEntityManagerFactory("TEST");
            }

            return emf;
        } finally {
            LOCK.unlock();
        }
    }

    public void shutdownEntityManagerFactory() {
        if(emf.isOpen()){
            emf.close();
        }
    }

    public EntityManager getEntityManager() {

        if (this.em != null && this.em.isOpen()) {
            return this.em;
        } else {
            this.em = getEntityManagerFactory().createEntityManager();
            return this.em;
        }

    }

}
