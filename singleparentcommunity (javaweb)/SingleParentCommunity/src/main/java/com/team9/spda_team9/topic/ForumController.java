package com.team9.spda_team9.topic;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

@RestController
@SessionAttributes("userdetails")
public class ForumController {

	@Autowired
	private ForumService forumService;
	
	@GetMapping("/getTopicByCategory")
	public List<Topic> getTopicByCategory(@RequestHeader() String category) throws InterruptedException, ExecutionException {
		return forumService.getTopicByCategory(category);
	}
	
	@PostMapping("/createTopic")
	public String createTopic(@RequestBody Topic topic) throws InterruptedException, ExecutionException {
		return forumService.saveTopic(topic);
	}
	
	@PutMapping("/updateTopic")
	public String updateUser(@RequestBody Topic topic) throws InterruptedException, ExecutionException {
		return forumService.updateTopic(topic);
	}
	
	@DeleteMapping("/deleteTopic")
	public String deleteUser(@RequestHeader() String topicId) throws InterruptedException, ExecutionException {
		return forumService.deleteTopic(topicId);
	}
}
