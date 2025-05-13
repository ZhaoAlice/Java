package spring.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import java.util.List;

public interface CategoryRepository extends Neo4jRepository<Category, Long> {
    List<Category> findByNameContaining(String name);
}