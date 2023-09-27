package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.AccidentService;

@Controller
@AllArgsConstructor
@RequestMapping("/accidents")
public class AccidentController {
    private final AccidentService accidents;

    @GetMapping("/create")
    public String viewCreateAccident() {
        return "accidents/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Accident accident) {
        accidents.create(accident);
        return "redirect:/index";
    }

    @GetMapping("/update")
    public String getByIdForUpdate(Model model, @RequestParam("id") int id) {
        var opt = accidents.read(id);
        if (opt.isEmpty()) {
            model.addAttribute("message",
                    "Данные с указанным идентификатором не найдены");
            return "errors/404";
        }
        model.addAttribute("accident", opt.get());
        return "accidents/edit";
    }

    @PostMapping("/update")
    public String update(Model model, @ModelAttribute Accident accident) {
        if (!accidents.update(accident)) {
            model.addAttribute("message", "Не удалось обновить lfyyst " + accident);
            return "errors/404";
        }
        return "redirect:/index";
    }

}