package pl.edu.libraryapi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.libraryapi.entity.Author;

import java.util.Optional;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {
    @Query("select a from Author a where a.firstName=:firstName and a.lastName=:lastName")
    Optional<Author> findByFullName(String firstName, String lastName);
}
