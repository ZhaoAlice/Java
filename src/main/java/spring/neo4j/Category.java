package spring.neo4j;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;

import java.util.HashSet;
import java.util.Set;

@Node("Category")
@Data
@NoArgsConstructor
public class Category {
    @Id @GeneratedValue
    private Long id;
    
    private String name;
    
    @Relationship(type = "BELONGS_TO", direction = Relationship.Direction.INCOMING)
    private Set<Book> books = new HashSet<>();
    
    public Category(String name) {
        this.name = name;
    }
    
    public void addBook(Book book) {
        books.add(book);
    }
}