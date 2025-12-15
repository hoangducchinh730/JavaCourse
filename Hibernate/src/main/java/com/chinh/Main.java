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

        SessionFactory sf = new Configuration()
                .addAnnotatedClass(com.chinh.Student.class)
                .configure()
                .buildSessionFactory();

        Session session = sf.openSession();

        Student s2 = session.find(Student.class, 8);

        session.close();
        sf.close();

        if (s2 != null)
            System.out.println(s2.getsName());
        else
            System.err.println("Không tồn tại trong database.");
    }
}