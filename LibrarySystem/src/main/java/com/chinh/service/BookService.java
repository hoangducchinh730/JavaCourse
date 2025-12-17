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

    public void createBook()
    {
        System.out.println("=== THÊM SÁCH MỚI ===");
        int id = Math.abs(new Random().nextInt());
        System.out.println("Nhập tên sách: ");
        String title = scanner.nextLine();
        System.out.println("Nhập tên tác giả: ");
        String author = scanner.nextLine();

        double price = 0;
        boolean validPrice = false;
        while(!validPrice)
        {
            System.out.println("Nhập giá: ");
            try {
                price = Double.parseDouble(scanner.nextLine());
                if (price < 0)
                    System.err.println("Giá không được âm. Vui lòng nhập lại.");
                else
                    validPrice = true;
            } catch (NumberFormatException e)
            {
                System.out.println("Lỗi: Vui lòng nhập đúng định dạng");
            }
        }

        Book newBook = new Book();
        newBook.setTitle(title);
        newBook.setAuthor(author);
        newBook.setBasePrice(price);
        bookRepository.save(newBook);

        System.out.println(">> Đã thêm sách thành công: " + title);
    }

    public void displayAllBooks()
    {
        System.out.println("=== DANH SÁCH SÁCH TRONG KHO ===");
        List<Book> bookList = bookRepository.findAll();

        if (bookList.isEmpty()) {
            System.out.println(">> Kho sách đang trống.");
        } else {
            // In định dạng bảng cho đẹp
            System.out.printf("%-10s %-30s %-20s %-10s\n", "ID", "TÊN SÁCH", "TÁC GIẢ", "GIÁ");
            System.out.println("--------------------------------------------------------------------------");
            for (Book b : bookList) {
                System.out.printf("%-10d %-30s %-20s %-10.1f\n",
                        b.getId(),
                        formatString(b.getTitle(), 30), // Cắt chuỗi nếu quá dài
                        formatString(b.getAuthor(), 20),
                        b.getBasePrice());
            }
        }
    }

    // Hàm phụ trợ để cắt chuỗi hiển thị cho đẹp
    private String formatString(String input, int maxLength) {
        if (input == null) return "";
        if (input.length() <= maxLength) return input;
        return input.substring(0, maxLength - 3) + "...";
    }
}
