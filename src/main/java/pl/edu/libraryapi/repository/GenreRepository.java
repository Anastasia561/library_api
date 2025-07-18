package pl.edu.libraryapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.libraryapi.entity.Genre;

import java.util.Optional;

@Repository
public interface GenreRepository extends CrudRepository<Genre, Long> {
    Optional<Genre> findByName(String name);
}
