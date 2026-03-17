package hoduyvu.lab04.repository;

import hoduyvu.lab04.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IAccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByMssv(String mssv);
    boolean existsByMssv(String mssv);
    boolean existsByRole(String role);
}
