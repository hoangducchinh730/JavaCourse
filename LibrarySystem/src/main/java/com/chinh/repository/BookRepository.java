package com.chinh.repository;

import com.chinh.entity.Book;
import com.chinh.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class BookRepository {

    public void save(Book book)
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            Transaction tx = session.beginTransaction();
            session.merge(book);
            tx.commit();
        }
    }

    public Book findById(int bookId)
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            return session.find(Book.class, bookId);
        }
    }

    public List<Book> findAll()
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            String hql = "FROM Book";
            Query<Book> query = session.createQuery(hql, Book.class);
            return query.getResultList();
        }
    }

    public void delete(int bookId)
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            Transaction tx = session.beginTransaction();
            Book bookToDelete = session.find(Book.class, bookId);
            if(bookToDelete != null)
                session.remove(bookToDelete);
            tx.commit();
        }
    }

    public Book findByIdWithBorrowers(int bookId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // LEFT JOIN FETCH: Lấy Sách VÀ lấy luôn danh sách borrowers (nếu có)
            // Dùng LEFT để nếu sách chưa ai mượn thì vẫn lấy được sách về
            String hql = "FROM Book b LEFT JOIN FETCH b.borrowers WHERE b.id = :id";

            Query<Book> query = session.createQuery(hql, Book.class);
            query.setParameter("id", bookId);

            return query.uniqueResult();
        }
    }
}
