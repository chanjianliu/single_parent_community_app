package com.team9.spda_team9.user;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IUserService {
	public String saveUser(User user) throws InterruptedException, ExecutionException;
	public List<User> getUsers() throws InterruptedException, ExecutionException;
	public User getUser(String username) throws InterruptedException, ExecutionException;
	public String updateUser(User user) throws InterruptedException, ExecutionException;
	public String deleteUser(String username);
	
	public List<User> searchUser(String key) throws InterruptedException, ExecutionException;
	public List<User> getAllUsers() throws InterruptedException, ExecutionException;
	public User getUserByUserId(String id) throws InterruptedException, ExecutionException;
}
