package hoduyvu.lab04.controller;

import hoduyvu.lab04.entity.MonHoc;
import hoduyvu.lab04.services.MonHocService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/monhoc")
public class MonHocController {

    @Autowired
    private MonHocService monHocService;

    @GetMapping("")
    public String showAllMonHoc(Model model) {
        List<MonHoc> dsMonHoc = monHocService.getAllMonHoc();
        model.addAttribute("dsMonHoc", dsMonHoc);
        return "monhoc/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("monHoc", new MonHoc());
        return "monhoc/add";
    }

    @PostMapping("/add")
    public String addMonHoc(@Valid @ModelAttribute("monHoc") MonHoc monHoc,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "monhoc/add";
        }
        monHocService.addMonHoc(monHoc);
        return "redirect:/monhoc";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        MonHoc monHoc = monHocService.getMonHocById(id);
        model.addAttribute("monHoc", monHoc);
        return "monhoc/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateMonHoc(@PathVariable String id,
                               @Valid @ModelAttribute("monHoc") MonHoc monHoc,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "monhoc/edit";
        }
        MonHoc currentMonHoc = monHocService.getMonHocById(id);
        if (currentMonHoc != null) {
            monHoc.setSinhViens(currentMonHoc.getSinhViens());
        }
        monHocService.updateMonHoc(monHoc);
        return "redirect:/monhoc";
    }

    @GetMapping("/delete/{id}")
    public String deleteMonHoc(@PathVariable String id) {
        monHocService.deleteMonHoc(id);
        return "redirect:/monhoc";
    }
}
