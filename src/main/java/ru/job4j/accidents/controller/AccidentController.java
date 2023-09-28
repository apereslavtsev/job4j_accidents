package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.service.AccidentService;

@Controller
@AllArgsConstructor
@RequestMapping("/accidents")
public class AccidentController {
    private final AccidentService accidents;

    @GetMapping("/create")
    public String viewCreateAccident(Model model) {
        List<AccidentType> types = new ArrayList<>();
        types.add(new AccidentType(1, "Две машины"));
        types.add(new AccidentType(2, "Машина и человек"));
        types.add(new AccidentType(3, "Машина и велосипед"));
        model.addAttribute("types", types);
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
        List<AccidentType> types = new ArrayList<>();
        types.add(new AccidentType(1, "Две машины"));
        types.add(new AccidentType(2, "Машина и человек"));
        types.add(new AccidentType(3, "Машина и велосипед"));
        model.addAttribute("types", types);
        model.addAttribute("accident", opt.get());
        return "accidents/edit";
    }

    @PostMapping("/update")
    public String update(Model model, @ModelAttribute Accident accident) {
        if (!accidents.update(accident)) {
            model.addAttribute("message", "Не удалось обновить данные " + accident);
            return "errors/404";
        }
        return "redirect:/index";
    }

}