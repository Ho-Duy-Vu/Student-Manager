package hoduyvu.lab04.controller;

import hoduyvu.lab04.entity.Lop;
import hoduyvu.lab04.entity.MonHoc;
import hoduyvu.lab04.entity.SinhVien;
import hoduyvu.lab04.services.LopService;
import hoduyvu.lab04.services.MonHocService;
import hoduyvu.lab04.services.SinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        List<SinhVien> dsSinhVien = sinhVienService.getAllSinhVien();
        List<Lop> dsLop = lopService.getAllLop();
        List<MonHoc> dsMonHoc = monHocService.getAllMonHoc();

        long totalSinhVien = dsSinhVien.size();
        long totalLop = dsLop.size();
        long totalMonHoc = dsMonHoc.size();
        long totalLuotDangKy = dsMonHoc.stream().mapToLong(MonHoc::getSoLuongDaDangKy).sum();

        // Thống kê sinh viên theo lớp
        Map<String, Integer> studentByClass = new LinkedHashMap<>();
        studentByClass.put("Chưa có lớp", 0);
        for (Lop lop : dsLop) {
            studentByClass.put(lop.getTenLop(), 0);
        }
        for (SinhVien sinhVien : dsSinhVien) {
            if (sinhVien.getLop() == null || sinhVien.getLop().getTenLop() == null) {
                studentByClass.put("Chưa có lớp", studentByClass.get("Chưa có lớp") + 1);
            } else {
                String tenLop = sinhVien.getLop().getTenLop();
                studentByClass.put(tenLop, studentByClass.getOrDefault(tenLop, 0) + 1);
            }
        }

        // Top môn học có lượt đăng ký cao nhất
        List<MonHoc> topMonHoc = new ArrayList<>(dsMonHoc);
        topMonHoc.sort(Comparator.comparingInt(MonHoc::getSoLuongDaDangKy).reversed());
        if (topMonHoc.size() > 6) {
            topMonHoc = topMonHoc.subList(0, 6);
        }

        List<String> topMonHocLabels = topMonHoc.stream().map(MonHoc::getTenMonHoc).toList();
        List<Integer> topMonHocData = topMonHoc.stream().map(MonHoc::getSoLuongDaDangKy).toList();

        int tongSucChuaMonHoc = dsMonHoc.stream()
                .map(MonHoc::getSoLuongDangKy)
                .filter(value -> value != null)
                .mapToInt(Integer::intValue)
                .sum();
        int tongDaDangKy = dsMonHoc.stream().mapToInt(MonHoc::getSoLuongDaDangKy).sum();

        model.addAttribute("totalSinhVien", totalSinhVien);
        model.addAttribute("totalLop", totalLop);
        model.addAttribute("totalMonHoc", totalMonHoc);
        model.addAttribute("totalLuotDangKy", totalLuotDangKy);

        model.addAttribute("classLabels", new ArrayList<>(studentByClass.keySet()));
        model.addAttribute("classData", new ArrayList<>(studentByClass.values()));
        model.addAttribute("topCourseLabels", topMonHocLabels);
        model.addAttribute("topCourseData", topMonHocData);
        model.addAttribute("capacityData", List.of(tongDaDangKy, Math.max(tongSucChuaMonHoc - tongDaDangKy, 0)));

        return "admin/dashboard";
    }
}
