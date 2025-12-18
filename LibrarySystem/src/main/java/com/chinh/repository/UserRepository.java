package com.chinh.repository;

import com.chinh.entity.User;
import com.chinh.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class UserRepository {
    public void save(User user)
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            Transaction tx = session.beginTransaction();
             session.merge(user);
             tx.commit();
        }
    }

    public User findByName(String name)
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            String hql = "FROM User WHERE name = :name ";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("name", name);
            User user = query.uniqueResult(); // Tạm chấp nhận tên là duy nhất.
            return user;
        }
    }

    public User findByIdWithBooks(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Lấy User VÀ danh sách borrowedBooks cùng lúc
            String hql = "FROM User u LEFT JOIN FETCH u.borrowedBooks WHERE u.id = :id";

            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("id", userId);

            return query.uniqueResult();
        }
    }
}
