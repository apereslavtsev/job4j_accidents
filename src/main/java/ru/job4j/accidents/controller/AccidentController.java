package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;


import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.AccidentService;
import ru.job4j.accidents.service.AccidentTypeService;
import ru.job4j.accidents.service.RuleService;

@Controller
@AllArgsConstructor
@RequestMapping("/accidents")
public class AccidentController {
    private final AccidentService accidents;
    private final AccidentTypeService accidentTypeService;
    private final RuleService ruleService;

    @GetMapping("/create")
    public String viewCreateAccident(Model model) {
        model.addAttribute("types", accidentTypeService.getAll());
        model.addAttribute("rules", ruleService.getAll());
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "accidents/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Accident accident, HttpServletRequest req) {        
        accidents.create(accident, req.getParameterValues("rIds"));
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
        Accident accident = opt.get();
        model.addAttribute("accident", accident);
        model.addAttribute("types", accidentTypeService.getAll());
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
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