package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;
import ru.job4j.todo.service.TimeZoneService;
import ru.job4j.todo.utilits.UserSession;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Controller
@AllArgsConstructor
@ThreadSafe
public class TaskController {

    public TaskService taskService;
    public PriorityService priorityService;
    public CategoryService categoryService;
    public TimeZoneService timeZoneService;

    @GetMapping("/addTask")
    public String addTask(Model model, HttpSession session) {
        User currentUser = UserSession.user(session);
        model.addAttribute("user", currentUser);
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("task", new Task());
        model.addAttribute("categories", categoryService.findAll());
        return "addTask";
    }

    @PostMapping("/createTask")
    public String createTask(@ModelAttribute Task task, @RequestParam("priority.id") int priorityId,
                             @RequestParam(name = "categoryId", required = false)
                             List<Integer> categoriesId,
                             HttpSession session) {
        var currentUserTimeZone = UserSession.user(session).getTimeZone();
        var time = LocalDateTime.now().atZone(
                ZoneId.of(String.format("UTC%s", currentUserTimeZone.getUtcOffset()))
        ).toLocalDateTime();
        task.setCreated(time);
        task.setPriority(priorityService.findById(priorityId));
        task.setCategories(categoryService.findByIds(categoriesId));
        task.setUser(UserSession.user(session));
        var regUser = taskService.add(task);
        if (regUser) {
            return "redirect:/index";
        }
        return "redirect:/failSave";
    }

    @GetMapping("/failSave")
    public String failSave(Model model, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("message", "Задача с таким именем уже существует");
        return "failSave";
    }

    @PostMapping("/failRedirectSave")
    public String failRedirectSave() {
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
    public String update(Model model, HttpSession session,
                         @PathVariable("taskID") int taskID) {
        model.addAttribute("task", taskService.findById(taskID));
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "updateTask";
    }

    @PostMapping("/updateTask")
    public String updateTask(Model model, HttpSession session,
                             @RequestParam("priority.id") int priorityId,
                             @ModelAttribute Task task,
                             @RequestParam(name = "categoryId", required = false)
                                 List<Integer> categoriesId) {
        var currentUser = UserSession.user(session);
        task.setPriority(priorityService.findById(priorityId));
        task.setCategories(categoryService.findByIds(categoriesId));
        task.setUser(currentUser);
        var rsl = taskService.update(task);
        if (rsl) {
            return String.format("redirect:/task/%s", task.getId());
        }
        model.addAttribute("user", currentUser);
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
