package hoduyvu.lab04.repository;

import hoduyvu.lab04.entity.SinhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISinhVienRepository extends JpaRepository<SinhVien, String> {
	List<SinhVien> findByLop_MaLop(Integer maLop);
}
