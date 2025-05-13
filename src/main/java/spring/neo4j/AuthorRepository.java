package spring.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AuthorRepository extends Neo4jRepository<Author, Long> {
    List<Author> findByNameContaining(String name);
    
    @Query("MATCH (a:Author)-[:AUTHORED_BY]->(b:Book) WHERE b.title = $bookTitle RETURN a")
    List<Author> findByBookTitle(@Param("bookTitle") String bookTitle);
}