package com.mytaxi.service.user;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mytaxi.dataaccessobject.UserRepository;
import com.mytaxi.domainobject.UserDO;

/**
 * @author Mohan Sharma Created on 24/09/18.
 */
@Service
public class MyTaxiUserDetailsService implements UserDetailsService
{
	private static final Logger LOG = LoggerFactory.getLogger(MyTaxiUserDetailsService.class);

	private UserRepository userRepository;

	@Autowired
	public MyTaxiUserDetailsService(final UserRepository userRepository)
	{
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		UserDO user = userRepository.findOneByUsername(username);
		return new User(user.getUsername(), user.getPassword(), Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
	}
}
