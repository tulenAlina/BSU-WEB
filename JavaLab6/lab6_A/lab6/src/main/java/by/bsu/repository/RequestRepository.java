package by.bsu.repository;

import by.bsu.entities.Request;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends CrudRepository<Request, Integer> {
    List<Request> findAll();
}