package hoduyvu.lab04.controller;

import hoduyvu.lab04.entity.MonHoc;
import hoduyvu.lab04.entity.SinhVien;
import hoduyvu.lab04.services.MonHocService;
import hoduyvu.lab04.services.SinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/courseregs")
public class CourseRegistrationController {

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private MonHocService monHocService;

    @GetMapping("")
    public String availableCourses(org.springframework.security.core.Authentication authentication, Model model) {
        SinhVien sinhVien = sinhVienService.getSinhVienById(authentication.getName());
        if (sinhVien == null) {
            return "redirect:/user/home";
        }
        Set<String> registeredCourseIds = new HashSet<>();
        if (sinhVien.getMonHocs() != null) {
            sinhVien.getMonHocs().forEach(monHoc -> registeredCourseIds.add(monHoc.getMaMon()));
        }

        model.addAttribute("allCourses", monHocService.getAllMonHoc());
        model.addAttribute("registeredCourseIds", registeredCourseIds);
        return "courseregs/list";
    }

    @GetMapping("/my-courses")
    public String myCourses(org.springframework.security.core.Authentication authentication, Model model) {
        SinhVien sinhVien = sinhVienService.getSinhVienById(authentication.getName());
        model.addAttribute("registeredCourses", sinhVien == null ? new HashSet<>() : sinhVien.getMonHocs());
        return "courseregs/my-courses";
    }

    @PostMapping("/register/{maMon}")
    public String registerCourse(@PathVariable String maMon,
                                 org.springframework.security.core.Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        SinhVien sinhVien = sinhVienService.getSinhVienById(authentication.getName());
        MonHoc monHoc = monHocService.getMonHocById(maMon);
        if (sinhVien == null || monHoc == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy môn học hoặc sinh viên.");
            return "redirect:/courseregs";
        }

        Set<MonHoc> monHocs = sinhVien.getMonHocs() == null ? new HashSet<>() : new HashSet<>(sinhVien.getMonHocs());
        if (monHocs.stream().anyMatch(course -> course.getMaMon().equals(maMon))) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn đã đăng ký môn học này rồi.");
            return "redirect:/courseregs";
        }

        if (!monHoc.isConCho()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Môn học này đã đủ số lượng đăng ký.");
            return "redirect:/courseregs";
        }

        monHocs.add(monHoc);
        sinhVien.setMonHocs(monHocs);
        sinhVienService.updateSinhVien(sinhVien);
        redirectAttributes.addFlashAttribute("successMessage", "Đăng ký môn học thành công.");
        return "redirect:/courseregs";
    }

    @PostMapping("/unregister/{maMon}")
    public String unregisterCourse(@PathVariable String maMon,
                                   org.springframework.security.core.Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        SinhVien sinhVien = sinhVienService.getSinhVienById(authentication.getName());
        if (sinhVien == null) {
            return "redirect:/courseregs/my-courses";
        }
        Set<MonHoc> monHocs = sinhVien.getMonHocs() == null ? new HashSet<>() : new HashSet<>(sinhVien.getMonHocs());
        monHocs.removeIf(monHoc -> monHoc.getMaMon().equals(maMon));
        sinhVien.setMonHocs(monHocs);
        sinhVienService.updateSinhVien(sinhVien);
        redirectAttributes.addFlashAttribute("successMessage", "Hủy đăng ký môn học thành công.");
        return "redirect:/courseregs/my-courses";
    }
}
