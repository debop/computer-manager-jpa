package repository;

import models.Computer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * domains.repository.ComputerRepository
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 2014. 1. 23. 오후 3:31
 */
@Repository
public interface ComputerJpaRepository extends JpaRepository<Computer, Long> {

    Page<Computer> findByNameLike(String filter, Pageable pageable);
}
