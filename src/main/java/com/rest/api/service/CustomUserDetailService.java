package com.rest.api.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.rest.api.advice.exception.CUserNotFoundException;
import com.rest.api.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String userPk) {
		return userRepository.findByUid(userPk).orElseThrow(CUserNotFoundException::new);
	}

}
