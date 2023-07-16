import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class Book {
    private String title;
    private String author;
    private int publicationYear;

    public Book(String title, String author, int publicationYear) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + ", Publication Year: " + publicationYear;
    }
}

class LibraryCatalog {
    private List<Book> books;

    public LibraryCatalog() {
        books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
        sortBooks();
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public Book searchBookByTitle(String title) {
        int index = binarySearchByTitle(title);
        if (index != -1) {
            return books.get(index);
        }
        return null;
    }

    private int binarySearchByTitle(String title) {
        int left = 0;
        int right = books.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comparison = books.get(mid).getTitle().compareTo(title);

            if (comparison == 0) {
                return mid;
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1;
    }

    private void sortBooks() {
        Collections.sort(books, Comparator.comparing(Book::getTitle));
    }

    public void saveCatalogToFile(String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(books);
            System.out.println("Catalog saved to file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving catalog to file: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadCatalogFromFile(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            books = (List<Book>) inputStream.readObject();
            System.out.println("Catalog loaded from file: " + fileName);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading catalog from file: " + e.getMessage());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        LibraryCatalog catalog = new LibraryCatalog();

        // Load catalog from file (if available)
        catalog.loadCatalogFromFile("library_catalog.ser");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nLibrary Book Search");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("3. Search Book by Title");
            System.out.println("4. Save Catalog to File");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter book author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter publication year: ");
                    int year = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    Book book = new Book(title, author, year);
                    catalog.addBook(book);
                    System.out.println("Book added to the catalog: " + book);
                    break;
                case 2:
                    System.out.print("Enter book title to remove: ");
                    String removeTitle = scanner.nextLine();

                    Book removeBook = catalog.searchBookByTitle(removeTitle);
                    if (removeBook != null) {
                        catalog.removeBook(removeBook);
                        System.out.println("Book removed from the catalog: " + removeBook);
                    } else {
                        System.out.println("Book not found: " + removeTitle);
                    }
                    break;
                case 3:
                    System.out.print("Enter book title to search: ");
                    String searchTitle = scanner.nextLine();

                    Book foundBook = catalog.searchBookByTitle(searchTitle);
                    if (foundBook != null) {
                        System.out.println("Book found: " + foundBook);
                    } else {
                        System.out.println("Book not found: " + searchTitle);
                    }
                    break;
                case 4:
                    catalog.saveCatalogToFile("library_catalog.ser");
                    break;
                case 5:
                    // Save catalog to file before exiting
                    catalog.saveCatalogToFile("library_catalog.ser");
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
