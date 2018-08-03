package game.SpringBoot.controller;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;


@Controller
public class MainController {

    @GetMapping("/")
    public String index(@SessionAttribute(SecurityValidate.SESSION_KEY) String account, Model model)
    {
        model.addAttribute("name", account);
        return "index";
    }

    @GetMapping("/loginView")
    public String login() 
    {
        return "login";
    }

    @PostMapping("/loginPost")
    public @ResponseBody Map<String, Object> loginPost(String account, String password, HttpSession session) 
    {
        Map<String, Object> map = new HashMap<>();
        if (!"123456".equals(password))
        {
            map.put("success", false);
            map.put("message", "密码错误");
            return map;
        }

        session.setAttribute(SecurityValidate.SESSION_KEY, account);

        map.put("success", true);
        map.put("message", "登录成功");
        return map;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) 
    {
        // 移除session
        session.removeAttribute(SecurityValidate.SESSION_KEY);
        return "redirect:/loginView";
    }

}