package com.team9.spda_team9.admin;

import java.util.concurrent.ExecutionException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.team9.spda_team9.user.IUserService;
import com.team9.spda_team9.user.User;
import com.team9.spda_team9.user.UserService;

@Controller
@SessionAttributes("userdetails")
public class AdminController {
	@Autowired
	private IAdminService adminService;

	@Autowired
	IUserService userService;

	@Autowired
	public AdminController(AdminService adminService, UserService userService) {
		this.adminService = adminService;
		this.userService = userService;
	}

	@GetMapping("/getAdminByUsername")
	public Admin getAdminByUsername(@RequestHeader() String username) throws InterruptedException, ExecutionException {
		return adminService.getAdminByUsername(username);
	}

	@PostMapping("/createAdmin")
	public String createAdmin(@RequestBody Admin admin) throws InterruptedException, ExecutionException {
		return adminService.createAdmin(admin);
	}

	@PostMapping("/updateAdmin")
	public String updateUser(@RequestBody Admin admin) throws InterruptedException, ExecutionException {
		return adminService.updateAdmin(admin);
	}

	@GetMapping("/deleteAdmin")
	public String deleteUser(@RequestHeader() String userId) throws InterruptedException, ExecutionException {
		return adminService.deleteAdmin(userId);
	}

	@RequestMapping(value = "/sa")
	public String sentimentalAnalysisPage() {
		return "sentimentalAnalysis";
	}

	@RequestMapping(value = "/match")
	public String matchingAlgoPage() {
		return "matchingAlgo";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String search(@RequestParam("keyword") String key, ModelMap model)
			throws InterruptedException, ExecutionException {
		List<User> users = null;

		if (!key.equals("") && key != null) {
			users = userService.searchUser(key);
		} else {
			users = userService.getUsers();
		}
		model.addAttribute("users", users);
		return "searchResult";
	}

	// logic on suspension
	@RequestMapping(value = "/detail/{id}")
	public String showUser(@PathVariable("id") String username, ModelMap model)
			throws InterruptedException, ExecutionException {
		User user = userService.getUser(username);
		if (user.isSuspended() == false) {
			model.addAttribute("suspendTo", "suspend");
		} else {
			model.addAttribute("suspendTo", "unsuspend");
		}

		model.addAttribute("user", user);
		return "Userdetail";
	}

	@RequestMapping(value = "/suspend/{id}")
	public String suspenduser(@PathVariable("id") String username, ModelMap model)
			throws InterruptedException, ExecutionException {
		User user = userService.getUser(username);
		user.setSuspended(true);
		userService.updateUser(user);
		return "redirect:/detail/"+ username;
	}

	@RequestMapping(value = "/unsuspend/{id}")
	public String unsuspenduser(@PathVariable("id") String username, ModelMap model)
			throws InterruptedException, ExecutionException {
		User user = userService.getUser(username);
		user.setSuspended(false);
		userService.updateUser(user);
		return "redirect:/detail/"+ username;
	}
	
	@RequestMapping(value = "/agreement/{id}")
	public String Showagreement(@PathVariable("id") String username, ModelMap model)
			throws InterruptedException, ExecutionException {
		User user = userService.getUser(username);
		model.addAttribute("user", user);
		return "agreement";
	}
}
