package com.team9.spda_team9.comment;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ICommentService {
	public String saveForum(Comment comment) throws InterruptedException, ExecutionException;
	public List<String> getCommentsByUserId(String userId) throws InterruptedException, ExecutionException;
	public String updateComment(Comment comment) throws InterruptedException, ExecutionException;
	public String deleteComment(String commentId) throws InterruptedException, ExecutionException;
	
	public List<Comment> getAllUserComment() throws InterruptedException, ExecutionException;
}
