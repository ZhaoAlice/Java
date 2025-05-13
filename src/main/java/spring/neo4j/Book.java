package spring.neo4j;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;
import java.util.HashSet;
import java.util.Set;

@Node("Book")
@Data
@NoArgsConstructor
public class Book {
    @Id @GeneratedValue
    private Long id;
    
    private String title;
    private String isbn;
    private Integer publicationYear;
    
    @Relationship(type = "AUTHORED_BY", direction = Relationship.Direction.INCOMING)
    private Set<Author> authors = new HashSet<>();
    
    @Relationship(type = "BELONGS_TO", direction = Relationship.Direction.OUTGOING)
    private Set<Category> categories = new HashSet<>();
    
    public Book(String title, String isbn, Integer publicationYear) {
        this.title = title;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
    }
    
    public void addAuthor(Author author) {
        authors.add(author);
    }
    
    public void addCategory(Category category) {
        categories.add(category);
    }
}