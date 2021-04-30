package com.team9.spda_team9.admin;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {
    @Autowired
    private IAdminService adminService;
    
    @Autowired
    private LoginService (AdminService adminService) {
    	this.adminService = adminService;
    }

   @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException  { 
	   Admin admin = null;
        
		try {
			admin = adminService.getAdminByUsername(username.toLowerCase());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        if (admin == null) {
            throw new UsernameNotFoundException("User does not exist");
            }
        return new MainUser(admin);
    }
}
