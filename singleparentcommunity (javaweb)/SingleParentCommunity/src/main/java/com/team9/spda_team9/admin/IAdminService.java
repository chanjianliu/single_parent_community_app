package com.team9.spda_team9.admin;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.team9.spda_team9.comment.Comment;

public interface IAdminService {
	public String createAdmin(Admin admin) throws InterruptedException, ExecutionException;
	public Admin getAdminByUsername(String username) throws InterruptedException, ExecutionException;
	public String updateAdmin(Admin admin) throws InterruptedException, ExecutionException;
	public String deleteAdmin(String userId) throws InterruptedException, ExecutionException;
	
	public boolean authenticate(LoginData admin) throws InterruptedException, ExecutionException;
}
