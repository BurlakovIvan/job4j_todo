package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;
import ru.job4j.todo.utilits.UserSession;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
@AllArgsConstructor
public class TaskController {

    public TaskService taskService;

    public PriorityService priorityService;

    @GetMapping("/addTask")
    public String addTask(Model model, HttpSession session) {
        User currentUser = UserSession.user(session);
        model.addAttribute("user", currentUser);
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("task",
                new Task(0, "Название", "Описание", LocalDateTime.now(), false, currentUser, null));
        return "addTask";
    }

    @PostMapping("/createTask")
    public String createTask(@ModelAttribute Task task, @RequestParam("priority.id") int priorityId,
                             HttpSession session) {
        task.setCreated(LocalDateTime.now());
        task.setPriority(priorityService.findById(priorityId));
        task.setUser(UserSession.user(session));
        taskService.add(task);
        return "redirect:/index";
    }

    @GetMapping("/completedTasksList")
    public String completedTasksList(Model model, HttpSession session) {
        User currentUser = UserSession.user(session);
        model.addAttribute("user", currentUser);
        model.addAttribute("tasks", taskService.findCompleted(currentUser));
        model.addAttribute("ALL", "false");
        model.addAttribute("COMPLETE", "true");
        model.addAttribute("NEWTask", "false");
        return "index";
    }

    @GetMapping("/newTasksList")
    public String newTasksList(Model model, HttpSession session) {
        User currentUser = UserSession.user(session);
        model.addAttribute("user", currentUser);
        model.addAttribute("tasks", taskService.findNew(currentUser));
        model.addAttribute("ALL", "false");
        model.addAttribute("COMPLETE", "false");
        model.addAttribute("NEWTask", "true");
        return "index";
    }

    @GetMapping("/task/{taskID}")
    public String thisSessions(Model model, HttpSession session, @PathVariable("taskID") int taskID) {
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("task", taskService.findById(taskID));
        return "task";
    }

    @GetMapping("/complete/{taskID}")
    public String complete(Model model, HttpSession session, @PathVariable("taskID") int taskID) {
        var rsl = taskService.complete(taskID);
        if (rsl) {
            return String.format("redirect:/task/%s", taskID);
        }
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("message", "ERROR");
        return "condition";
    }

    @GetMapping("/update/{taskID}")
    public String update(Model model, HttpSession session, @PathVariable("taskID") int taskID) {
        model.addAttribute("task", taskService.findById(taskID));
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("priorities", priorityService.findAll());
        return "updateTask";
    }

    @PostMapping("/updateTask")
    public String updateTask(Model model, HttpSession session, @RequestParam("priority.id") int priorityId,
                             @ModelAttribute Task task) {
        task.setPriority(priorityService.findById(priorityId));
        var rsl = taskService.update(task);
        if (rsl) {
            return String.format("redirect:/task/%s", task.getId());
        }
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("message", "ERROR");
        return "condition";
    }

    @GetMapping("/delete/{taskID}")
    public String delete(Model model, HttpSession session, @PathVariable("taskID") int taskID) {
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("message", taskService.delete(taskID) ? "SUCCESS" : "Failed to delete task");
        return "condition";
    }
}
