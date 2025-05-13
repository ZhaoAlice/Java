package spring.neo4j;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;

import java.util.HashSet;
import java.util.Set;

@Node("Author")
@Data
@NoArgsConstructor
public class Author {
    @Id @GeneratedValue
    private Long id;
    
    private String name;
    private String country;
    
    @Relationship(type = "AUTHORED_BY", direction = Relationship.Direction.OUTGOING)
    private Set<Book> books = new HashSet<>();
    
    public Author(String name, String country) {
        this.name = name;
        this.country = country;
    }
    
    public void addBook(Book book) {
        books.add(book);
    }
}