package com.chinh.repository;

import com.chinh.Book;
import com.chinh.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class BookRepository {
    private final HibernateUtil hibernateUtil;
    public BookRepository(HibernateUtil hibernateUtil)
    {
        this.hibernateUtil = hibernateUtil;
    }

    public void save(Book book)
    {
        Session session = hibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.merge(book);

        tx.commit();
        session.close();
    }

    public Book findById(int bookId)
    {
        Session session = hibernateUtil.getSessionFactory().openSession();

        Book book = session.find(Book.class, bookId);

        session.close();

        return book;
    }

    public List<Book> findAll()
    {
        String hql = "FROM Book";
        Session session = hibernateUtil.getSessionFactory().openSession();

        Query query = session.createQuery(hql, Book.class);
        List<Book> bookList = query.getResultList();

        session.close();
        return  bookList;
    }

    public void delete(int bookId)
    {
        Session session = hibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Book bookToDelete = session.find(Book.class, bookId);
        if (bookToDelete == null) return;
        session.remove(bookToDelete);

        tx.commit();
        session.close();
    }
}
