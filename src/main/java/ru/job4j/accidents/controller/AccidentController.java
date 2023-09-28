package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.assertj.core.util.Arrays;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;
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
        List<Rule> rules = List.of(
                new Rule(1, "Статья. 1"),
                new Rule(2, "Статья. 2"),
                new Rule(3, "Статья. 3"));
        model.addAttribute("rules", rules);
        return "accidents/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Accident accident, HttpServletRequest req) {
        String[] rIdsAr = req.getParameterValues("rIds");
        if (rIdsAr != null && rIdsAr.length > 0) {
            List<String> rIds = List.of();
            accident.setRules(rIds.stream()
                    .map(id -> new Rule(Integer.parseInt(id), ""))
                    .collect(Collectors.toSet())
                );
        }
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