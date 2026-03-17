package hoduyvu.lab04.controller;

import hoduyvu.lab04.entity.SinhVien;
import hoduyvu.lab04.services.SinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SinhVienService sinhVienService;

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        String mssv = authentication.getName();
        SinhVien sinhVien = sinhVienService.getSinhVienById(mssv);
        model.addAttribute("sinhVien", sinhVien);
        return "user/home";
    }
}
