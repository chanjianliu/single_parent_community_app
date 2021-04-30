package com.team9.spda_team9.admin;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.team9.spda_team9.user.IUserService;
import com.team9.spda_team9.user.User;
import com.team9.spda_team9.user.UserService;

@Controller
@SessionAttributes("userdetails")
public class LoginController {
	
	@Autowired
	private IAdminService adminService;
	
	@Autowired
	IUserService userService;
	
	@Autowired
	public LoginController (AdminService adminService, UserService userService) {
		this.adminService = adminService;
		this.userService = userService;
	}
	
	@GetMapping(value="/index")
	public String index(ModelMap model) throws InterruptedException, ExecutionException {
		List<User> users = userService.getUsers();
		model.addAttribute("users", users);
		return "index";
	}
	
	@GetMapping("/login")
    public String login(ModelMap model, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        model.addAttribute("admin", new LoginData());
        return "login";
    }
	
	@GetMapping("/logout")
    public String logout(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "login";
    }
	
	@GetMapping("/error")
    public String forbidden() {
        return "error";
    }
	
	@PostMapping("/authenticate")
	public String authenticate(@ModelAttribute("admin") LoginData admin) throws InterruptedException, ExecutionException {
		
		if (adminService.authenticate(admin))
			return "redirect:/index";
		else
			return "redirect:/login";
	}
	
	@GetMapping("/")
    public String getdashboard(@AuthenticationPrincipal MainUser mainUser, HttpServletRequest request, ModelMap model, HttpSession httpSession) 
    		throws InterruptedException, ExecutionException {

        if(mainUser != null) {

        	String username = mainUser.getUsername();
            Admin admin = adminService.getAdminByUsername(username);

            httpSession.setAttribute("userdetails", admin);
            
            List<User> users = userService.getUsers();
    		model.addAttribute("users", users);
    		return "index";
        } else {
        	return "redirect:/login";
        }
	}
}
