package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.todo.service.TaskService;

@Controller
@AllArgsConstructor
public class IndexController {

    public TaskService taskService;

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("ALL", "true");
        model.addAttribute("COMPLETE", "false");
        model.addAttribute("NEWTask", "false");
        return "index";
    }
}
