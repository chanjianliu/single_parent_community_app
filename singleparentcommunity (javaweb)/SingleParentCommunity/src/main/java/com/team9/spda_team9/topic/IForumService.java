package com.team9.spda_team9.topic;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IForumService {
	public String saveTopic(Topic topic) throws InterruptedException, ExecutionException;
	public List<Topic> getTopicByCategory(String category) throws InterruptedException, ExecutionException;
	public String updateTopic(Topic topic) throws InterruptedException, ExecutionException;
	public String deleteTopic(String topicId);
}
