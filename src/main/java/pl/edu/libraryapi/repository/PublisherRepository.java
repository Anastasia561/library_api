package pl.edu.libraryapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.libraryapi.entity.Publisher;

import java.util.Optional;

@Repository
public interface PublisherRepository extends CrudRepository<Publisher, Long> {
    Optional<Publisher> findByName(String name);
}
