package br.com.imovelcontrol.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SegurancaController {

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal User user) {
        if (user != null) {
            return "index";
        }
        return "Login";

    }

    @PostMapping("/login")
    public String loginPost() {
        return "Login";
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView paginaInicial(@AuthenticationPrincipal User user) {
        if (user != null) {
            return new ModelAndView("redirect:/dashboard");
        }
        return new ModelAndView("Login");
    }

    @GetMapping("/403")
    public String acessoNegado() {
        return "403";
    }
}
