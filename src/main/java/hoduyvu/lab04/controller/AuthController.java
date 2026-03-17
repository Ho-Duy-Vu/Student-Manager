package hoduyvu.lab04.controller;

import hoduyvu.lab04.entity.Account;
import hoduyvu.lab04.entity.SinhVien;
import hoduyvu.lab04.repository.IAccountRepository;
import hoduyvu.lab04.services.SinhVienService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("account", new Account());
        return "auth/register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid @ModelAttribute("account") Account account,
                             BindingResult bindingResult,
                             @RequestParam("matKhauRaw") String matKhauRaw,
                             Model model) {

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        if (matKhauRaw == null || matKhauRaw.length() < 6) {
            model.addAttribute("errorMatKhau", "Mật khẩu phải có ít nhất 6 ký tự.");
            return "auth/register";
        }

        if (accountRepository.existsByMssv(account.getMssv())) {
            model.addAttribute("errorMssv", "MSSV này đã được đăng ký tài khoản.");
            return "auth/register";
        }

        // Tạo SinhVien TRƯỚC để tránh FK constraint violation
        SinhVien sinhVien = sinhVienService.getSinhVienById(account.getMssv());
        if (sinhVien == null) {
            sinhVien = new SinhVien();
            sinhVien.setMssv(account.getMssv());
            sinhVien.setHoTen(account.getHoTen());
            sinhVien.setEmail(""); // Đặt email rỗng, sẽ cập nhật sau
            sinhVienService.addSinhVien(sinhVien);
        }

        // Tạo Account sau khi SinhVien đã tồn tại
        account.setMatKhau(passwordEncoder.encode(matKhauRaw));
        account.setRole("ROLE_USER");
        accountRepository.save(account);

        return "redirect:/auth/login?registered=true";
    }
}
