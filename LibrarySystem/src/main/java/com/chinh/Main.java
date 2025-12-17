package com.chinh;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class Main {
    private static SessionFactory sf;
    public static void main(String[] args)
    {
        Configuration cfg = new Configuration();
        cfg.addAnnotatedClass(User.class)
                .addAnnotatedClass(Book.class)
                .addAnnotatedClass(LibraryCard.class);
        cfg.configure();

        sf = cfg.buildSessionFactory();

        testL2Cache(1);

        sf.close();
    }

    // TEST1: Lazy Loading
    private static void testLazyLoading(int userId)
    {
        Session session = sf.openSession();

        System.out.println("--- BẮT ĐẦU FIND USER ---");
        User user = session.find(User.class, userId);

        System.out.println("User tên là: " + user.getName());
        System.out.println("--- SAU KHI IN TÊN, CHUẨN BỊ GỌI LIST SÁCH ---");

        List<Book> books = user.getBorrowedBooks();
        System.out.println("Số sách đang mượn: " + books.size());

        session.close();
    }

    private static void testHQLQuery(String authorName)
    {
        Session session = sf.openSession();

        System.out.println("--- TÌM SÁCH CỦA TÁC GIẢ: " + authorName + " ---");
        String hql = "FROM Book WHERE author = :auth";
        Query<Book> query = session.createQuery(hql, Book.class);
        query.setParameter("auth", authorName);
        List<Book> result = query.getResultList();
        for(Book b: result)
        {
            System.out.println(b);
        }
        session.close();
    }

    private static void testUpdateUser(int userId, String newName)
    {
        Session session = sf.openSession();

        Transaction tx = session.beginTransaction();  // Nhớ mở transaction không là nó không update đâu cái này khác với trước là chỉ đọc.
        User userToUpdate = session.find(User.class, userId);

        if (userToUpdate != null)
        {
            userToUpdate.setName(newName);
            session.merge(userToUpdate);
            System.out.println("Nhập nhật tên user thành: " + newName);
        }

        tx.commit();
        session.close();
    }

    private static void testDeleteBook(int bookId)
    {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        Book bookToDelete = session.find(Book.class, bookId);

        if (bookToDelete != null)
        {
            for(User user: bookToDelete.getBorrowers())
            {
                user.getBorrowedBooks().remove(bookToDelete);
            }

            session.remove(bookToDelete);
            System.out.println("Đã xóa sách ID: " + bookId);
        }
        else
        {
            System.out.println("Sách không tồn tại.");
        }

        tx.commit();
        sf.close();
    }

    private static void testL1Cache(int bookId)
    {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        System.out.println("--- Lần 1: Load book ---");
        Book b1 = session.find(Book.class, bookId);
        System.out.println("Tên sách là: " + b1.getTitle());

        System.out.println("--- Lần 2: Load book cùng id ---");
        Book b2 = session.find(Book.class, bookId);
        System.out.println("Tên sách là: " + b2.getTitle());

        System.out.println("--- Kiểm chúng xem b1 và b2 có trỏ tới cùng một vùng nhớ trên heap (RAM) không: " + (b1 == b2));

        tx.commit();
        session.close();
    }

    private static void testGetReference(int userId)
    {
        Session session = sf.openSession();

        System.out.println("--- Gọi getReference ---");
        User user = session.getReference(User.class, userId);

        System.out.println("--- Đã có user Object (Proxy) ---");

        System.out.println("--- Bây giờ mới truy cập dữ liệu ---");
        System.out.println("User name: " + user.getName());

        session.close();
    }

    private static void testSelectiveFetch()
    {
        Session session = sf.openSession();

        System.out.println("--- Chỉ lấy Tên sách và Tác giả (Tiết kiệm RAM) ---");

        String hql = "SELECT title, author FROM Book";
        Query<Object[]> query = session.createQuery(hql, Object[].class);

        List<Object[]> results = query.getResultList();
        for(var row: results)
        {
            String title = (String)row[0];
            String author = (String)row[1];
            System.out.println("Sách " + title + " được viết bởi tác giả " + author);
        }

        session.close();
    }

    private static void testL2Cache(int bookId) {
        System.out.println("--- Session 1 bắt đầu ---");
        Session session1 = sf.openSession();
        session1.beginTransaction();

        Book b1 = session1.find(Book.class, bookId);
        System.out.println("User A xem sách: " + b1.getTitle());

        session1.getTransaction().commit();
        session1.close();

        System.out.println("\n--- Session 2 bắt đầu (Sau khi Session 1 đã đóng) ---");
        Session session2 = sf.openSession();
        session2.beginTransaction();

        Book b2 = session2.find(Book.class, bookId);
        System.out.println("User B xem sách: " + b2.getTitle());

        session2.getTransaction().commit();
        session2.close();
    }
}
