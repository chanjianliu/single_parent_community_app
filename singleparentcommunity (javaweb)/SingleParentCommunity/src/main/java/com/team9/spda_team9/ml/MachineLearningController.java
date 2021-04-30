package com.team9.spda_team9.ml;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.team9.spda_team9.comment.Comment;
import com.team9.spda_team9.comment.CommentService;
import com.team9.spda_team9.comment.ICommentService;
import com.team9.spda_team9.topic.ForumService;
import com.team9.spda_team9.topic.IForumService;
import com.team9.spda_team9.user.IUserService;
import com.team9.spda_team9.user.User;
import com.team9.spda_team9.user.UserService;

@RestController
public class MachineLearningController {

	@Autowired
	private IForumService forumService;

	@Autowired
	private ICommentService commentService;

	@Autowired
	private IUserService userService;
	
	@Autowired
	public MachineLearningController(ForumService forumService, CommentService commentService, UserService userService) {
		this.forumService = forumService;
		this.commentService = commentService;
		this.userService = userService;
	}
	
	
	//for matching algorithm
	@Async
	@PostMapping("/askForMacthes")
	public ModelAndView invokeMatchingAlgo() {
		String link = "http://127.0.0.1:5002/matchingcalculation";
		HttpURLConnection conn = null;
		String output = null;
		
		try {
			URL url = new URL(link);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
			
            if (conn.getResponseCode() != 200) {
            	System.out.print("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                //System.out.println("result for user" + userId + " = " + output);
            }
            
            conn.disconnect();
            
		} catch (MalformedURLException e) { 
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.disconnect();
		}
		
		return getMatchingResultFromPython();
	}
	
	
	
	@GetMapping("/getusers")
	public List<User> getAllUser() throws InterruptedException, ExecutionException {
		return userService.getAllUsers(); 
	}
	
	
	
	@GetMapping("/getMatches")
	public ModelAndView getMatchingResultFromPython(){

				String link = "http://127.0.0.1:5002/matchingcalculation";
				HttpURLConnection conn = null;
				String output = null;
				String result = "";
				
				try {

					URL url = new URL(link);
					conn = (HttpURLConnection) url.openConnection();
					conn.setDoOutput(true);
					conn.setRequestMethod("GET");
					conn.setRequestProperty("Content-Type", "application/json");
		            conn.setRequestProperty("charset", "utf-8");

					
		            if (conn.getResponseCode() != 200) {
		            	System.out.print("Failed : HTTP error code : "
		                        + conn.getResponseCode());
		            }
		            
		            BufferedReader br = new BufferedReader(new InputStreamReader(
		                    (conn.getInputStream())));

		            System.out.println("Output from Server .... \n");
		            while ((output = br.readLine()) != null) {
		                System.out.println("result: " + " = " + output);
		                result += output;
		            }
		            
		            conn.disconnect();
		            
				} catch (MalformedURLException e) { 
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (conn != null)
						conn.disconnect();
				}
				
				 ModelAndView mv = new ModelAndView("AFUserMatching");
			     return mv;
	}
	
	
	//for Sentimental Analysis
	@GetMapping("/getcomments")
	public List<Comment> getAllComment() throws InterruptedException, ExecutionException {
		return commentService.getAllUserComment();
	}

	@GetMapping("/getTextByUserId/{id}")
	public List<String> getTextByUserId(@PathVariable("id") String userId)
			throws InterruptedException, ExecutionException {
		return commentService.getCommentsByUserId(userId);
	}

	@GetMapping("/getResult")
	public ModelAndView getResultFromPython(ModelMap model) {

		String link = "http://127.0.0.1:5001/sacalculation";
		HttpURLConnection conn = null;
		String output = null;
		String result = "";
		
		try {
			URL url = new URL(link);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("charset", "utf-8");

			if (conn.getResponseCode() != 200) {
				System.out.print("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			
			int count = 0;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println("result: " + " = " + output);
				result += output;
//				System.out.println(result);
			}
			//pending debug
//			String time = Long.toString(System.currentTimeMillis());
//			Firestore dbFirestore = FirestoreClient.getFirestore();
//			ApiFuture<WriteResult> future = dbFirestore.collection("SentimentalScore").document(time).set(result.trim());
			
			conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.disconnect();
		}

		Map<String, String> results = new HashMap<>();
		results.put("result", result);
		
		model.addAttribute("results", results);
		 ModelAndView mv = new ModelAndView("AFSentimentalAnalysis");
	        return mv;
	}
	
	@PostMapping("/checkSA")
	public ModelAndView invokeSentimentalAnalysis(ModelMap model) { // argument should be userId
		// String link = "http://127.0.0.1:5000/model1?x=" + userId;
		String link = "http://127.0.0.1:5001/test";
//		String input = "12";
		HttpURLConnection conn = null;
//		DataOutputStream os = null;
		String output = null;

		try {
//			byte[] postData = input.getBytes(StandardCharsets.UTF_8);
			URL url = new URL(link);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("charset", "utf-8");
//            conn.setRequestProperty("Content-Length", Integer.toString(input.length()));

//            os = new DataOutputStream(conn.getOutputStream());
//            os.write(postData);
//            os.flush();

			if (conn.getResponseCode() != 200) {
				System.out.print("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				// System.out.println("result for user" + userId + " = " + output);
			}

			conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.disconnect();
		}
		
		
		return getResultFromPython(model);
	}

	@Async
	@PostMapping("/checkSA/{id}")
	public CompletableFuture<String> invokeSentimentalAnalysis(@PathVariable("id") String userId) { // argument should
																									// be userId
		// String link = "http://127.0.0.1:5000/model1?x=" + userId;
		String link = "http://127.0.0.1:5001/test?x=" + userId;
//		String input = "12";
		HttpURLConnection conn = null;
//		DataOutputStream os = null;
		String output = null;

		try {
//			byte[] postData = input.getBytes(StandardCharsets.UTF_8);
			URL url = new URL(link);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("charset", "utf-8");
//            conn.setRequestProperty("Content-Length", Integer.toString(input.length()));

//            os = new DataOutputStream(conn.getOutputStream());
//            os.write(postData);
//            os.flush();

			if (conn.getResponseCode() != 200) {
				System.out.print("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println("result for user" + userId + " = " + output);
			}

			conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.disconnect();
		}

		return CompletableFuture.completedFuture(output);
	}
}
