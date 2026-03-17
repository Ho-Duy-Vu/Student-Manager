package hoduyvu.lab04.services;

import hoduyvu.lab04.entity.Lop;
import hoduyvu.lab04.repository.ILopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LopService {
    @Autowired
    private ILopRepository lopRepository;

    public List<Lop> getAllLop() {
        return lopRepository.findAll();
    }

    public Lop getLopById(Integer id) {
        return lopRepository.findById(id).orElse(null);
    }

    public void addLop(Lop lop) {
        lopRepository.save(lop);
    }

    public void deleteLop(Integer id) {
        lopRepository.deleteById(id);
    }

    public void updateLop(Lop lop) {
        lopRepository.save(lop);
    }
}
