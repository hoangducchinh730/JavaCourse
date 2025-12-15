package com.chinh;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {
        Student s1 = new Student();
        s1.setRollNo(4);
        s1.setsName("Hùng");
        s1.setAge(13);

//        Configuration cfg = new Configuration();
//        cfg.addAnnotatedClass(com.chinh.Student.class);
//        cfg.configure();
//        SessionFactory sf = cfg.buildSessionFactory();

        Student s2 = null;

        SessionFactory sf = new Configuration()
                .addAnnotatedClass(com.chinh.Student.class)
                .configure()
                .buildSessionFactory();

        Session session = sf.openSession();

        Transaction  transaction = session.beginTransaction();

        session.persist(s1);

        transaction.commit();
        session.close();
        sf.close();

        System.out.println(s1);
    }
}