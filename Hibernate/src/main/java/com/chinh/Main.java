package com.chinh;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {
        Student s1 = new Student();
        s1.setRollNo(1);
        s1.setsName("Chinh");
        s1.setAge(10);

        SessionFactory sf = new Configuration()
                .addAnnotatedClass(com.chinh.Student.class)
                .configure()
                .buildSessionFactory();

        Session session = sf.openSession();

        session.merge(s1);

        session.close();
        sf.close();

        if (s1 != null)
            System.out.println(s1.getsName());
        else
            System.err.println("Không tồn tại trong database.");
    }
}