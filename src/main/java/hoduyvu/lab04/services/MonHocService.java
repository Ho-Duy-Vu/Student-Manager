package hoduyvu.lab04.services;

import hoduyvu.lab04.entity.MonHoc;
import hoduyvu.lab04.repository.IMonHocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonHocService {
    @Autowired
    private IMonHocRepository monHocRepository;
    
    public List<MonHoc> getAllMonHoc() {
        return monHocRepository.findAll();
    }

    public MonHoc getMonHocById(String id) {
        return monHocRepository.findById(id).orElse(null);
    }

    public List<MonHoc> getAllMonHocByIds(List<String> ids) {
        return monHocRepository.findAllById(ids);
    }

    public void addMonHoc(MonHoc monHoc) {
        monHocRepository.save(monHoc);
    }

    public void deleteMonHoc(String id) {
        monHocRepository.deleteById(id);
    }

    public void updateMonHoc(MonHoc monHoc) {
        monHocRepository.save(monHoc);
    }
}
