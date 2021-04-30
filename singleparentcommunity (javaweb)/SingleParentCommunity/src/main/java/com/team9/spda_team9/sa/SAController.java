package com.team9.spda_team9.sa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.team9.spda_team9.admin.AdminService;
import com.team9.spda_team9.user.IUserService;
import com.team9.spda_team9.user.User;
import com.team9.spda_team9.user.UserService;


@Controller
public class SAController {
	
	@Autowired
	private ISAService SAService;
	@Autowired
	IUserService userService;
	
	@Autowired
	public SAController(SAService SAService, UserService userService) {
		this.SAService = SAService;
		this.userService = userService;

	}
	

	
	

//	@GetMapping("/getsascores")
//	public String getScores(Model model) throws InterruptedException, ExecutionException {
//		HashMap<String, Score> scoresMap = SAService.getSAScores(); 
//		HashMap<String, ArrayList<Float>> toBeSent = new HashMap<String, ArrayList<Float>>();
//		for (Entry<String, Score> pair : scoresMap.entrySet()) {
//			
//			ArrayList<Float> saScores = new ArrayList<Float>();
//		    System.out.println(String.format("Key (name) is: %s, Value (age) is : %s", pair.getKey(), pair.getValue()));   
//		    String userId = pair.getKey();
//		    saScores.add(pair.getValue().getPos());
//		    saScores.add(pair.getValue().getNeu());
//		    saScores.add(pair.getValue().getNeg());
//		    toBeSent.put(userId, saScores);
//		}
//		model.addAttribute("scoresmap",toBeSent);
//		
//		return "sascores";
//	}
	
	@GetMapping("/getsascores")
	public String getScores(ModelMap model) throws InterruptedException, ExecutionException {
		HashMap<String, Score> scoresMap = SAService.getSAScores(); 
		HashMap<String,String> toBeSent = new HashMap<String,String>();
		//HashMap<String, ArrayList<Float>> toBeSent = new HashMap<String, ArrayList<Float>>();
		String timestamp = SAService.getTimestamp();
		for (Entry<String, Score> pair : scoresMap.entrySet()) { 
			String userId = pair.getKey();
			
			User u = userService.getUserByUserId(userId);
			
			String username = null;
			
//			if(u == null) {
//				username = "";
			if(u != null){
				username = u.getUsername();
				String userScores = String.format("Positive : %s, Neutral : %s, Negative : %s ", 
			    		pair.getValue().getPos(), pair.getValue().getNeu(), pair.getValue().getNeg()); 
			    System.out.println(userId);
			    System.out.println(userScores);
			    System.out.println("-----------");
			    
			    toBeSent.put(username, userScores);
			}
			
//		    String userScores = String.format("Positive : %s, Neutral : %s, Negative : %s    (Calculating Timestamp: %s)", 
//		    		pair.getValue().getPos(), pair.getValue().getNeu(), pair.getValue().getNeg(),pair.getValue().getTimestamp() ); 
			
		}
		
		
		model.addAttribute("scoresmap",toBeSent);
		model.addAttribute("timestamp",timestamp);
		
		return "sascores";
	}
	
	
	
	

}
