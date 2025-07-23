import java.util.ArrayList;
import java.util.List;

public class Borrower {
    private String name;
    private String id;
    private List<Book> borrowedBooks;

    public Borrower(String name, String id) {
        this.name = name;
        this.id = id;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getName() { return name; }
    public String getId() { return id; }
    public List<Book> getBorrowedBooks() { return borrowedBooks; }

    public void borrowBook(Book book) {
        borrowedBooks.add(book);
        book.setAvailable(false);
    }

    public void returnBook(Book book) {
        borrowedBooks.remove(book);
        book.setAvailable(true);
    }

    @Override
    public String toString() {
        return name + " (ID: " + id + ") - Borrowed: " + borrowedBooks.size();
    }
}