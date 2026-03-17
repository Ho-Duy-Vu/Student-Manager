package hoduyvu.lab04.controller;

import hoduyvu.lab04.services.LopService;
import hoduyvu.lab04.services.MonHocService;
import hoduyvu.lab04.services.SinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private LopService lopService;

    @Autowired
    private MonHocService monHocService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long totalSinhVien = sinhVienService.getAllSinhVien().size();
        long totalLop = lopService.getAllLop().size();
        long totalMonHoc = monHocService.getAllMonHoc().size();

        model.addAttribute("totalSinhVien", totalSinhVien);
        model.addAttribute("totalLop", totalLop);
        model.addAttribute("totalMonHoc", totalMonHoc);

        return "admin/dashboard";
    }
}
