package repository;

import models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * domains.repository.CompanyJpaRepository
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 2014. 1. 23. 오후 3:16
 */
@Repository
public interface CompanyJpaRepository extends JpaRepository<Company, Long> {
}
