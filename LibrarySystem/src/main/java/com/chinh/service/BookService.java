package com.chinh.service;

import com.chinh.Book;
import com.chinh.repository.BookRepository;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

public class BookService {
    private final Scanner scanner;
    private final BookRepository bookRepository;

    public BookService(Scanner scanner, BookRepository bookRepository)
    {
        this.scanner = scanner;
        this.bookRepository = bookRepository;
    }

    public Book createBook()
    {
        int id = new Random().nextInt();
        System.out.println("Nhập tên sách: ");
        String title = scanner.nextLine();
        System.out.println("Nhập tên tác giả: ");
        String author = scanner.nextLine();
        System.out.println("Nhập giá: ");
        double price = Double.parseDouble(scanner.nextLine());

        Book newBook = new Book(id, title, author, price);
        bookRepository.save(newBook);
        return newBook;
    }

    public void displayAllBooks()
    {
        List<Book> bookList = bookRepository.findAll();
        bookList.forEach(System.out::println);
    }
}
