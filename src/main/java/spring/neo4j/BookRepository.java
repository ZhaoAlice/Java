package spring.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BookRepository extends Neo4jRepository<Book, Long> {
    List<Book> findByTitleContaining(String title);
    
    @Query("MATCH (b:Book)-[:AUTHORED_BY]->(a:Author) WHERE a.name = $authorName RETURN b")
    List<Book> findByAuthorName(@Param("authorName") String authorName);
    
    @Query("MATCH (b:Book)-[:BELONGS_TO]->(c:Category) WHERE c.name = $categoryName RETURN b")
    List<Book> findByCategoryName(@Param("categoryName") String categoryName);
}