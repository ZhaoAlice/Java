package spring.neo4j;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }
    
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
    
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElse(null);
    }
    
    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }
    
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
    
    public List<Author> findByNameContaining(String name) {
        return authorRepository.findByNameContaining(name);
    }
    
    public List<Author> findByBookTitle(String bookTitle) {
        return authorRepository.findByBookTitle(bookTitle);
    }
}