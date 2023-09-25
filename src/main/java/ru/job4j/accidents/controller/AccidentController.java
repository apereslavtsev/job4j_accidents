package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("update/{id}")
    public String getByIdForUpdate(Model model, @PathVariable int id) {
        Optional<Accident> accidentOptional = accidents.read(id);
        model.addAttribute("accident", accidentOptional.get());
        return "accidents/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Accident accident) {
        accidents.update(accident);
        return "redirect:/index";
    }

}