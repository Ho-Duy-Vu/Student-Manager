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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/accounts")
public class AdminAccountController {

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("accounts", accountRepository.findAll());
        return "admin/accounts/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("account", new Account());
        return "admin/accounts/add";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            return "redirect:/admin/accounts";
        }
        model.addAttribute("account", account);
        return "admin/accounts/edit";
    }

    @PostMapping("/add")
    public String addAccount(@Valid @ModelAttribute("account") Account account,
                             BindingResult bindingResult,
                             @RequestParam("matKhauRaw") String matKhauRaw,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/accounts/add";
        }

        if (accountRepository.existsByMssv(account.getMssv())) {
            model.addAttribute("errorMssv", "MSSV này đã có tài khoản.");
            return "admin/accounts/add";
        }

        if (matKhauRaw == null || matKhauRaw.length() < 6) {
            model.addAttribute("errorMatKhau", "Mật khẩu phải có ít nhất 6 ký tự.");
            return "admin/accounts/add";
        }

        SinhVien sinhVien = sinhVienService.getSinhVienById(account.getMssv());
        if (sinhVien == null) {
            sinhVien = new SinhVien();
            sinhVien.setMssv(account.getMssv());
            sinhVien.setHoTen(account.getHoTen());
            sinhVien.setEmail("");
            sinhVienService.addSinhVien(sinhVien);
        } else {
            sinhVien.setHoTen(account.getHoTen());
            sinhVienService.updateSinhVien(sinhVien);
        }

        account.setMatKhau(passwordEncoder.encode(matKhauRaw));
        if (account.getRole() == null || account.getRole().isBlank()) {
            account.setRole("ROLE_USER");
        }
        accountRepository.save(account);
        return "redirect:/admin/accounts";
    }

    @PostMapping("/edit/{id}")
    public String updateAccount(@PathVariable Long id,
                                @Valid @ModelAttribute("account") Account formAccount,
                                BindingResult bindingResult,
                                Model model) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            return "redirect:/admin/accounts";
        }

        if (bindingResult.hasErrors()) {
            return "admin/accounts/edit";
        }
        SinhVien sinhVien = sinhVienService.getSinhVienById(account.getMssv());
        if (sinhVien == null) {
            sinhVien = new SinhVien();
            sinhVien.setMssv(account.getMssv());
            sinhVien.setEmail("");
        }
        sinhVien.setHoTen(formAccount.getHoTen());
        sinhVienService.updateSinhVien(sinhVien);

        account.setHoTen(formAccount.getHoTen());
        account.setRole(formAccount.getRole());
        accountRepository.save(account);
        return "redirect:/admin/accounts";
    }

    @PostMapping("/password/{id}")
    public String resetPassword(@PathVariable Long id,
                                @RequestParam("newPassword") String newPassword,
                                Model model) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            return "redirect:/admin/accounts";
        }

        if (newPassword == null || newPassword.length() < 6) {
            model.addAttribute("accounts", accountRepository.findAll());
            model.addAttribute("errorPassword", "Mật khẩu mới phải có ít nhất 6 ký tự.");
            model.addAttribute("passwordAccountId", id);
            return "admin/accounts/list";
        }

        account.setMatKhau(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
        return "redirect:/admin/accounts";
    }

    @PostMapping("/role/{id}")
    public String updateRole(@PathVariable Long id, @RequestParam("role") String role) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account != null) {
            account.setRole(role);
            accountRepository.save(account);
        }
        return "redirect:/admin/accounts";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        accountRepository.deleteById(id);
        return "redirect:/admin/accounts";
    }
}
