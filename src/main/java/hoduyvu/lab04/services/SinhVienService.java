package hoduyvu.lab04.services;

import hoduyvu.lab04.entity.SinhVien;
import hoduyvu.lab04.entity.Account;
import hoduyvu.lab04.repository.IAccountRepository;
import hoduyvu.lab04.repository.ISinhVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SinhVienService {
    @Autowired
    private ISinhVienRepository sinhVienRepository;

    @Autowired
    private IAccountRepository accountRepository;

    public List<SinhVien> getAllSinhVien() {
        return sinhVienRepository.findAll();
    }

    public SinhVien getSinhVienById(String id) {
        return sinhVienRepository.findById(id).orElse(null);
    }

    public void addSinhVien(SinhVien sinhVien) {
        sinhVienRepository.save(sinhVien);
    }

    @Transactional
    public void deleteSinhVien(String id) {
        // Xóa Account liên quan trước để tránh vi phạm khóa ngoại
        accountRepository.findByMssv(id).ifPresent(accountRepository::delete);
        sinhVienRepository.deleteById(id);
    }

    public void updateSinhVien(SinhVien sinhVien) {
        Account account = accountRepository.findByMssv(sinhVien.getMssv()).orElse(null);
        if (account != null) {
            account.setHoTen(sinhVien.getHoTen());
            accountRepository.save(account);
        }
        sinhVienRepository.save(sinhVien);
    }
}
