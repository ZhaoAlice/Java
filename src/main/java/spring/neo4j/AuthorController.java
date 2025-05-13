package spring.neo4j;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService authorService;
    
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }
    
    @GetMapping
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }
    
    @GetMapping("/{id}")
    public Author getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }
    
    @PostMapping
    public Author createAuthor(@RequestBody Author author) {
        return authorService.saveAuthor(author);
    }
    
    @PutMapping("/{id}")
    public Author updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        author.setId(id);
        return authorService.saveAuthor(author);
    }
    
    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }
    
    @GetMapping("/search")
    public List<Author> searchAuthors(@RequestParam(required = false) String name,
                                     @RequestParam(required = false) String bookTitle) {
        if (name != null) {
            return authorService.findByNameContaining(name);
        } else if (bookTitle != null) {
            return authorService.findByBookTitle(bookTitle);
        }
        return authorService.getAllAuthors();
    }
}