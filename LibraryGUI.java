import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LibraryGUI extends JFrame {
    private Library library;
    private DefaultListModel<String> bookListModel;
    private DefaultListModel<String> borrowerListModel;

    public LibraryGUI() {
        library = new Library();
        
        // Sample data
        library.addBook("Java Programming", "John Doe", "123-456-789");
        library.addBook("Data Structures", "Jane Smith", "987-654-321");
        library.addBook("Algorithms", "Robert Johnson", "456-123-789");
        library.addBorrower("Alice", "B001");
        library.addBorrower("Bob", "B002");

        setTitle("Library Tracker System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        bookListModel = new DefaultListModel<>();
        borrowerListModel = new DefaultListModel<>();
        updateBookList();
        updateBorrowerList();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Books", createBooksPanel());
        tabbedPane.addTab("Borrowers", createBorrowersPanel());
        tabbedPane.addTab("Transactions", createTransactionsPanel());
        
        add(tabbedPane);
    }

    private JPanel createBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Book list
        JList<String> bookList = new JList<>(bookListModel);
        JScrollPane scrollPane = new JScrollPane(bookList);
        
        // Search panel
        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton resetButton = new JButton("Reset");
        
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText();
            List<Book> results = library.searchBooks(keyword);
            
            DefaultListModel<String> resultModel = new DefaultListModel<>();
            for (Book book : results) {
                resultModel.addElement(book.toString());
            }
            bookList.setModel(resultModel);
        });
        
        resetButton.addActionListener(e -> {
            searchField.setText("");
            bookList.setModel(bookListModel);
        });
        
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);
        
        // Add book panel (unchanged)
        JPanel addBookPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField isbnField = new JTextField();
        JButton addButton = new JButton("Add Book");
        
        addButton.addActionListener(e -> {
            if (!titleField.getText().isEmpty() && !authorField.getText().isEmpty() && !isbnField.getText().isEmpty()) {
                library.addBook(titleField.getText(), authorField.getText(), isbnField.getText());
                updateBookList();
                titleField.setText("");
                authorField.setText("");
                isbnField.setText("");
            }
        });
        
        addBookPanel.add(new JLabel("Title:"));
        addBookPanel.add(titleField);
        addBookPanel.add(new JLabel("Author:"));
        addBookPanel.add(authorField);
        addBookPanel.add(new JLabel("ISBN:"));
        addBookPanel.add(isbnField);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(addBookPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(southPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createBorrowersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Borrower list
        JList<String> borrowerList = new JList<>(borrowerListModel);
        JScrollPane scrollPane = new JScrollPane(borrowerList);
        
        // Add borrower panel (unchanged)
        JPanel addBorrowerPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();
        JButton addButton = new JButton("Add Borrower");
        
        addButton.addActionListener(e -> {
            if (!nameField.getText().isEmpty() && !idField.getText().isEmpty()) {
                library.addBorrower(nameField.getText(), idField.getText());
                updateBorrowerList();
                nameField.setText("");
                idField.setText("");
            }
        });
        
        addBorrowerPanel.add(new JLabel("Name:"));
        addBorrowerPanel.add(nameField);
        addBorrowerPanel.add(new JLabel("ID:"));
        addBorrowerPanel.add(idField);
        
        // Improved Show books button
        JButton showBooksButton = new JButton("Show Borrowed Books");
        showBooksButton.addActionListener(e -> {
            int index = borrowerList.getSelectedIndex();
            if (index != -1) {
                Borrower borrower = library.getAllBorrowers().get(index);
                
                // Create a formatted display
                JTextArea textArea = new JTextArea(10, 30);
                textArea.setEditable(false);
                textArea.append("Borrower: " + borrower.getName() + " (ID: " + borrower.getId() + ")\n\n");
                textArea.append("Borrowed Books:\n");
                
                if (borrower.getBorrowedBooks().isEmpty()) {
                    textArea.append("No books currently borrowed\n");
                } else {
                    for (Book book : borrower.getBorrowedBooks()) {
                        textArea.append("- " + book.getTitle() + " (ISBN: " + book.getIsbn() + ")\n");
                    }
                }
                
                JScrollPane scroll = new JScrollPane(textArea);
                JOptionPane.showMessageDialog(this, scroll, "Borrowed Books Details", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a borrower first", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        buttonPanel.add(addButton);
        buttonPanel.add(showBooksButton);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(addBorrowerPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(southPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Form panel (unchanged)
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        JLabel borrowerLabel = new JLabel("Borrower ID:");
        JTextField borrowerField = new JTextField();
        JLabel bookLabel = new JLabel("Book ISBN:");
        JTextField bookField = new JTextField();
        JButton borrowButton = new JButton("Borrow Book");
        JButton returnButton = new JButton("Return Book");
        
        // Status label
        JLabel statusLabel = new JLabel(" ", JLabel.CENTER);
        statusLabel.setForeground(Color.BLUE);
        
        // Action listeners (unchanged)
        borrowButton.addActionListener(e -> {
            String borrowerId = borrowerField.getText();
            String bookIsbn = bookField.getText();
            
            if (library.borrowBook(borrowerId, bookIsbn)) {
                statusLabel.setText("Book borrowed successfully!");
                statusLabel.setForeground(Color.GREEN);
                updateBookList();
                updateBorrowerList();
            } else {
                statusLabel.setText("Failed to borrow book. Check IDs and availability.");
                statusLabel.setForeground(Color.RED);
            }
        });
        
        returnButton.addActionListener(e -> {
            String borrowerId = borrowerField.getText();
            String bookIsbn = bookField.getText();
            
            if (library.returnBook(borrowerId, bookIsbn)) {
                statusLabel.setText("Book returned successfully!");
                statusLabel.setForeground(Color.GREEN);
                updateBookList();
                updateBorrowerList();
            } else {
                statusLabel.setText("Failed to return book. Check borrower ID and book ISBN.");
                statusLabel.setForeground(Color.RED);
            }
        });
        
        formPanel.add(borrowerLabel);
        formPanel.add(borrowerField);
        formPanel.add(bookLabel);
        formPanel.add(bookField);
        formPanel.add(borrowButton);
        formPanel.add(returnButton);
        
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void updateBookList() {
        bookListModel.clear();
        for (Book book : library.getAllBooks()) {
            bookListModel.addElement(book.toString());
        }
    }
    
    private void updateBorrowerList() {
        borrowerListModel.clear();
        for (Borrower borrower : library.getAllBorrowers()) {
            borrowerListModel.addElement(borrower.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryGUI gui = new LibraryGUI();
            gui.setVisible(true);
        });
    }
}