package repository;

import models.Computer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA 를 사용하는 Repository 입니다.
 * NOTE: QueryDSL은 Play framework 에서는 사용이 불가합니다. 이럴 때는 Domain 을 따로 Java Project 로 생성하여 참조해야합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 2014. 1. 23. 오후 3:31
 */
@Repository
public interface ComputerJpaRepository extends JpaRepository<Computer, Long> {

    Page<Computer> findByNameLike(String filter, Pageable pageable);
}
