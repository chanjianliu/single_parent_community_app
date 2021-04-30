package com.team9.spda_team9.sa;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.team9.spda_team9.comment.Comment;



public interface ISAService {
	public HashMap<String, Score> getSAScores() throws InterruptedException, ExecutionException;
	public String getTimestamp()throws InterruptedException, ExecutionException;

}
