package com.chinh.service;

import com.chinh.entity.Address;
import com.chinh.entity.Book;
import com.chinh.entity.LibraryCard;
import com.chinh.entity.User;
import com.chinh.repository.BookRepository;
import com.chinh.repository.UserRepository;

import java.util.List;
import java.util.Scanner;

public class UserService {
    private final Scanner scanner;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    public UserService(
            Scanner scanner,
            UserRepository userRepository,
            BookRepository bookRepository
    )
    {
        this.scanner = scanner;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public void registerUser()
    {
        System.out.println("Nhập tên user: ");
        String userName = scanner.nextLine();
        System.out.println("Nhập tên đường: ");
        String street = scanner.nextLine();
        System.out.println("Nhập tên thành phố: ");
        String city = scanner.nextLine();
        System.out.println("Nhập zipcode: ");
        String zipcode = scanner.nextLine();

        Address address = new Address(street, city, zipcode);
        User user = new User(userName, address);
        LibraryCard card = new LibraryCard();
        user.setCard(card);

        card.setUser(user); // Đảm bảo quan hệ hai chiều
        userRepository.save(user);
    }

    public User login()
    {
        System.out.println("Nhập tên đăng nhập: ");
        String userName = scanner.nextLine();
        User user = userRepository.findByName(userName);
        if (user == null)
        {
            System.out.println("Không tìm thấy user.");
            return null;
        }
        else
        {
            System.out.println("Xin chào " + userName);
            return user;
        }
    }

//    public void borrowBook(User currentUser)
//    {
//        System.out.println("Nhập id sách cần mượn: ");
//        int bookToBorrowId = Integer.parseInt(scanner.nextLine());
//        Book book = bookRepository.findById(bookToBorrowId);
//        if (book == null) {
//            System.out.println("Không tìm thấy sách.");
//            return;
//        }
//        List<User> borrowers = book.getBorrowers();
//        if (borrowers.size() >= 5) {
//            System.out.println("Đã có 5 người mượn cuốn sách này rồi (Max).");
//            return;
//        }
//        borrowers.add(currentUser);
//        currentUser.getBorrowedBooks().add(book);
//        userRepository.save(currentUser);
//        System.out.println("Mượn sách thành công!");
//    }

    public void showMyBooks(User currentUser)
    {
        List<Book> borrowedBooks = currentUser.getBorrowedBooks();
        System.out.println("--- TỦ SÁCH CỦA TÔI ---");
        borrowedBooks.forEach(System.out::println);
    }

//    public void returnBook(User currentUser)
//    {
//        showMyBooks(currentUser);
//        System.out.println("Nhập id sách muốn trả: ");
//        int bookToReturnId =  Integer.parseInt(scanner.nextLine());
//        boolean isBorrowed = false;
//        Book bookToReturn = null;
//        for(var book : currentUser.getBorrowedBooks())
//        {
//            if(bookToReturnId == book.getId())
//            {
//                isBorrowed = true;
//                bookToReturn = book;
//                break;
//            }
//        }
//        if (!isBorrowed)
//        {
//            System.out.println("Bạn chưa mượn cuốn sách này.");
//            return;
//        }
//        currentUser.getBorrowedBooks().remove(bookToReturn);
//        bookToReturn.getBorrowers().remove(currentUser);
//        userRepository.save(currentUser);
//        System.out.println("Trả sách thành công!");
//    }


    public void returnBook(User currentUser) {
        // 1. Load User "tươi" từ DB (kèm list sách)
        User freshUser = userRepository.findByIdWithBooks(currentUser.getId());

        showMyBooks(freshUser);

        System.out.println("Nhập id sách muốn trả: ");
        int bookToReturnId = Integer.parseInt(scanner.nextLine());

        // 2. Load Sách "tươi" từ DB (kèm list người mượn)
        // Lưu ý: Phải load sách này để đảm bảo ta có object để remove khỏi list user
        Book bookToReturn = bookRepository.findByIdWithBorrowers(bookToReturnId);

        if (bookToReturn == null) {
            System.out.println("Sách không tồn tại!");
            return;
        }

        // 3. Kiểm tra xem User có đang mượn sách này không
        // Nhờ đã override equals(), hàm .contains() sẽ hoạt động đúng
        if (!freshUser.getBorrowedBooks().contains(bookToReturn)) {
            System.out.println("Bạn chưa mượn cuốn sách này.");
            return;
        }

        // 4. Xóa quan hệ 2 chiều (QUAN TRỌNG)
        // Xóa sách khỏi User
        freshUser.getBorrowedBooks().remove(bookToReturn);

        // Xóa User khỏi sách (Cần remove object freshUser chứ không phải currentUser)
        bookToReturn.getBorrowers().remove(freshUser);

        // 5. Lưu lại User "tươi" (Cái mà ta vừa chỉnh sửa)
        userRepository.save(freshUser);

        System.out.println("Trả sách thành công!");
    }

    public void borrowBook(User currentUser) { // currentUser ở đây chỉ dùng để lấy ID
        System.out.println("Nhập id sách cần mượn: ");
        int bookToBorrowId = Integer.parseInt(scanner.nextLine());

        // 1. Lấy sách "tươi" kèm theo list borrowers (đã init)
        Book book = bookRepository.findByIdWithBorrowers(bookToBorrowId);

        if (book == null) {
            System.out.println("Không tìm thấy sách.");
            return;
        }

        // 2. Lấy User "tươi" kèm theo list borrowedBooks (đã init)
        //    Chúng ta dùng ID của currentUser để load lại từ DB
        User freshUser = userRepository.findByIdWithBooks(currentUser.getId());

        // 3. Logic check số lượng (lúc này book.getBorrowers() đã an toàn)
        if (book.getBorrowers().size() >= 5) {
            System.out.println("Đã có 5 người mượn cuốn sách này rồi (Max).");
            return;
        }

        // 4. Thao tác trên object "tươi" (Fresh Object)
        book.getBorrowers().add(freshUser);
        freshUser.getBorrowedBooks().add(book);

        // 5. Lưu lại
        userRepository.save(freshUser);
        System.out.println("Mượn sách thành công!");

        // MẸO: Cập nhật lại tham chiếu currentUser ở ngoài Main nếu cần thiết
        // (Vì Java truyền tham trị, nên việc gán freshUser ở đây ko ảnh hưởng Main,
        // nhưng vì ta đã save xuống DB nên lần sau load lại sẽ đúng).
    }
}
