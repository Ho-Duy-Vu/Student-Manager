package hoduyvu.lab04.controller;

import hoduyvu.lab04.entity.SinhVien;
import hoduyvu.lab04.services.LopService;
import hoduyvu.lab04.services.MonHocService;
import hoduyvu.lab04.services.SinhVienService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/sinhvien")
public class SinhVienController {

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private LopService lopService;

    @Autowired
    private MonHocService monHocService;

    @GetMapping("")
    public String showAllSinhVien(Model model) {
        List<SinhVien> dsSinhVien = sinhVienService.getAllSinhVien();
        model.addAttribute("dsSinhVien", dsSinhVien);
        return "sinhvien/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("sinhVien", new SinhVien());
        model.addAttribute("dsLop", lopService.getAllLop());
        model.addAttribute("dsMonHoc", monHocService.getAllMonHoc());
        return "sinhvien/add";
    }

    @PostMapping("/add")
    public String addSinhVien(@Valid @ModelAttribute("sinhVien") SinhVien sinhVien,
                              BindingResult bindingResult,
                              @RequestParam(value = "maLop", required = false) Integer maLop,
                              @RequestParam(value = "maMons", required = false) List<String> maMons,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("dsLop", lopService.getAllLop());
            model.addAttribute("dsMonHoc", monHocService.getAllMonHoc());
            return "sinhvien/add";
        }
        if (maLop != null) {
            sinhVien.setLop(lopService.getLopById(maLop));
        }
        if (maMons != null && !maMons.isEmpty()) {
            sinhVien.setMonHocs(new HashSet<>(monHocService.getAllMonHocByIds(maMons)));
        } else {
            sinhVien.setMonHocs(new HashSet<>());
        }
        sinhVienService.addSinhVien(sinhVien);
        return "redirect:/sinhvien";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id,
                               @RequestParam(value = "incomplete", required = false, defaultValue = "false") boolean incomplete,
                               Authentication authentication,
                               Model model) {
        if (!canAccessStudentProfile(id, authentication)) {
            return "redirect:/access-denied";
        }
        SinhVien sinhVien = sinhVienService.getSinhVienById(id);
        model.addAttribute("sinhVien", sinhVien);
        model.addAttribute("dsLop", lopService.getAllLop());
        if (incomplete) {
            model.addAttribute("incompleteMsg",
                    "Chào mừng! Vui lòng bổ sung thông tin còn thiếu (Ngày sinh, Email, Lớp) để hoàn tất hồ sơ sinh viên.");
        }
        return "sinhvien/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateSinhVien(@PathVariable String id,
                                 @Valid @ModelAttribute("sinhVien") SinhVien sinhVien,
                                 BindingResult bindingResult,
                                 @RequestParam(value = "maLop", required = false) Integer maLop,
                                 Authentication authentication,
                                 Model model) {
        if (!canAccessStudentProfile(id, authentication)) {
            return "redirect:/access-denied";
        }
        sinhVien.setMssv(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("dsLop", lopService.getAllLop());
            return "sinhvien/edit";
        }
        SinhVien currentSinhVien = sinhVienService.getSinhVienById(id);
        if (currentSinhVien != null) {
            sinhVien.setMonHocs(currentSinhVien.getMonHocs());
        }
        if (maLop != null) {
            sinhVien.setLop(lopService.getLopById(maLop));
        } else if (currentSinhVien != null) {
            sinhVien.setLop(currentSinhVien.getLop());
        }
        sinhVienService.updateSinhVien(sinhVien);
        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        return isAdmin ? "redirect:/sinhvien" : "redirect:/user/home";
    }

    @GetMapping("/delete/{id}")
    public String deleteSinhVien(@PathVariable String id) {
        sinhVienService.deleteSinhVien(id);
        return "redirect:/sinhvien";
    }

    private boolean canAccessStudentProfile(String mssv, Authentication authentication) {
        if (authentication == null) {
            return false;
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        return isAdmin || authentication.getName().equals(mssv);
    }
}
