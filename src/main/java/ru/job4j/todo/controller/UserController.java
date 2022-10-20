package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;
import ru.job4j.todo.utilits.UserSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Data
@AllArgsConstructor
@Controller
@ThreadSafe
public class UserController {

    private final UserService userService;

    @GetMapping("/formAddUser")
    public String addUser(Model model, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        return "addUser";
    }

    @PostMapping("/registration")
    public String registration(Model model, @ModelAttribute User user) {
        var regUser = userService.add(user);
        if (regUser) {
            return "redirect:/index";
        }
        return "redirect:/fail";
    }

    @GetMapping("/fail")
    public String failRegistration(Model model) {
        User userSession = new User();
        userSession.setName("Гость");
        model.addAttribute("user", userSession);
        model.addAttribute("message", "Пользователь с таким логином уже существует");
        return "fail";
    }

    @PostMapping("/failRedirect")
    public String failRedirect(Model model) {
        return "redirect:/formAddUser";
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model, @RequestParam(name = "fail", required = false) Boolean fail) {
        model.addAttribute("fail", fail != null);
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpServletRequest req) {
        Optional<User> userDb = userService.findUserByLoginAndPwd(
                user.getLogin(), user.getPassword()
        );
        if (userDb.isEmpty()) {
            return "redirect:/loginPage?fail=true";
        }
        HttpSession session = req.getSession();
        session.setAttribute("user", userDb.get());
        return "redirect:/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginPage";
    }
}
