package com.chinh.config;

import com.chinh.entity.Book;
import com.chinh.entity.LibraryCard;
import com.chinh.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory; // Vì biến này không thuộc về một instance

    public static SessionFactory getSessionFactory()
    {
        if (sessionFactory == null)
        {
            try
            {
                sessionFactory = new Configuration()
                        .addAnnotatedClass(User.class)
                        .addAnnotatedClass(Book.class)
                        .addAnnotatedClass(LibraryCard.class)
                        .configure() // Read the hibernate.cfg.xml
                        .buildSessionFactory();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.err.println("Lỗi khởi tạo SessionFactory");
            }
        }
        return sessionFactory;
    }

    public void shutdown()
    {
        if(sessionFactory != null)
            sessionFactory.close();
    }
}
