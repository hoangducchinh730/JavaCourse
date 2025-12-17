package com.chinh.config;

import com.chinh.Book;
import com.chinh.LibraryCard;
import com.chinh.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory; // Vì biến này không thuộc về một instance

    public static SessionFactory getSessionFactory()
    {
        if (sessionFactory == null)
        {
            sessionFactory = new Configuration()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Book.class)
                .addAnnotatedClass(LibraryCard.class)
                .configure() // Read the hibernate.cfg.xml
                .buildSessionFactory();

        }
        return sessionFactory;
    }

    public void shutdown()
    {
        sessionFactory.close();
    }
}
