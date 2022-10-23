package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.TaskService;
import ru.job4j.todo.utilits.UserSession;

import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
public class IndexController {

    public TaskService taskService;

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        User currentUser = UserSession.user(session);
        model.addAttribute("user", currentUser);
        model.addAttribute("tasks", taskService.findAll(currentUser));
        model.addAttribute("ALL", "true");
        model.addAttribute("COMPLETE", "false");
        model.addAttribute("NEWTask", "false");
        return "index";
    }
}
