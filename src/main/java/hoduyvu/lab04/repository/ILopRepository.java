package hoduyvu.lab04.repository;

import hoduyvu.lab04.entity.Lop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILopRepository extends JpaRepository<Lop, Integer> {
}
