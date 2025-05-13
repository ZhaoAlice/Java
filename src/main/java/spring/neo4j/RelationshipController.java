package spring.neo4j;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/relationships")
public class RelationshipController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    
    public RelationshipController(BookService bookService,
                                  AuthorService authorService,
                                  CategoryService categoryService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }
    
    @PostMapping("/book-author")
    public Book addAuthorToBook(@RequestParam Long bookId, @RequestParam Long authorId) {
        Book book = bookService.getBookById(bookId);
        Author author = authorService.getAuthorById(authorId);
        
        if (book != null && author != null) {
            book.addAuthor(author);
            return bookService.saveBook(book);
        }
        return null;
    }
    
    @PostMapping("/book-category")
    public Book addCategoryToBook(@RequestParam Long bookId, @RequestParam Long categoryId) {
        Book book = bookService.getBookById(bookId);
        Category category = categoryService.getCategoryById(categoryId);
        
        if (book != null && category != null) {
            book.addCategory(category);
            return bookService.saveBook(book);
        }
        return null;
    }
    
    @DeleteMapping("/book-author")
    public void removeAuthorFromBook(@RequestParam Long bookId, @RequestParam Long authorId) {
        Book book = bookService.getBookById(bookId);
        Author author = authorService.getAuthorById(authorId);
        
        if (book != null && author != null) {
            book.getAuthors().remove(author);
            bookService.saveBook(book);
        }
    }
    
    @DeleteMapping("/book-category")
    public void removeCategoryFromBook(@RequestParam Long bookId, @RequestParam Long categoryId) {
        Book book = bookService.getBookById(bookId);
        Category category = categoryService.getCategoryById(categoryId);
        
        if (book != null && category != null) {
            book.getCategories().remove(category);
            bookService.saveBook(book);
        }
    }
}