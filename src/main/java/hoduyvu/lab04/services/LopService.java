package hoduyvu.lab04.services;

import hoduyvu.lab04.entity.Lop;
import hoduyvu.lab04.entity.SinhVien;
import hoduyvu.lab04.repository.ILopRepository;
import hoduyvu.lab04.repository.ISinhVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LopService {
    @Autowired
    private ILopRepository lopRepository;

    @Autowired
    private ISinhVienRepository sinhVienRepository;

    public List<Lop> getAllLop() {
        return lopRepository.findAll();
    }

    public Lop getLopById(Integer id) {
        return lopRepository.findById(id).orElse(null);
    }

    public void addLop(Lop lop) {
        lopRepository.save(lop);
    }

    @Transactional
    public void deleteLop(Integer id) {
        List<SinhVien> dsSinhVien = sinhVienRepository.findByLop_MaLop(id);
        for (SinhVien sinhVien : dsSinhVien) {
            sinhVien.setLop(null);
        }
        sinhVienRepository.saveAll(dsSinhVien);
        lopRepository.deleteById(id);
    }

    public void updateLop(Lop lop) {
        lopRepository.save(lop);
    }
}
