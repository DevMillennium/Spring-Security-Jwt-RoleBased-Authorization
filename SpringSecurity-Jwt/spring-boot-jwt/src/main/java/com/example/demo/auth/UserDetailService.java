package com.example.demo.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserDetailService implements UserDetailsService{

	private Map<String,String> users = new HashMap<>();
	
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
    @PostConstruct
    public void init() {
        users.put("emirhan", passwordEncoder.encode("123"));
    }
	
	@Override
	// kullanıcı adını parametre alır ve kullanıcı kimliği nesnesini döndürür
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		//gelen username varsa 
        if (users.containsKey(username)) {
        	// ikinci parametre password olarak veriliyor
            return new User(username, users.get(username), new ArrayList<>());
        }

        throw new UsernameNotFoundException(username);
    }

}
