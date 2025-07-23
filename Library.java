import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Book> books;
    private List<Borrower> borrowers;

    public Library() {
        this.books = new ArrayList<>();
        this.borrowers = new ArrayList<>();
    }

    public void addBook(String title, String author, String isbn) {
        books.add(new Book(title, author, isbn));
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                book.getAuthor().toLowerCase().contains(keyword.toLowerCase()) ||
                book.getIsbn().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(book);
            }
        }
        return results;
    }

    public void addBorrower(String name, String id) {
        borrowers.add(new Borrower(name, id));
    }

    public Borrower findBorrower(String id) {
        for (Borrower borrower : borrowers) {
            if (borrower.getId().equals(id)) {
                return borrower;
            }
        }
        return null;
    }

    public Book findBook(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    public boolean borrowBook(String borrowerId, String bookIsbn) {
        Borrower borrower = findBorrower(borrowerId);
        Book book = findBook(bookIsbn);
        
        if (borrower != null && book != null && book.isAvailable()) {
            borrower.borrowBook(book);
            return true;
        }
        return false;
    }

    public boolean returnBook(String borrowerId, String bookIsbn) {
        Borrower borrower = findBorrower(borrowerId);
        Book book = findBook(bookIsbn);
        
        if (borrower != null && book != null && !book.isAvailable() && 
            borrower.getBorrowedBooks().contains(book)) {
            borrower.returnBook(book);
            return true;
        }
        return false;
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public List<Borrower> getAllBorrowers() {
        return borrowers;
    }
}