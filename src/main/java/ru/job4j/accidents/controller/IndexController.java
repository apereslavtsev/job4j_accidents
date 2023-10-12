package ru.job4j.accidents.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;
import ru.job4j.accidents.service.AccidentService;

@Controller
@AllArgsConstructor
public class IndexController {

    private AccidentService accidentService;

    @GetMapping({"/", "/index", "/accidents"})
    public String getIndex(Model model) {
       model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("accidents", accidentService.getAll());
        return "index";
    }
}
