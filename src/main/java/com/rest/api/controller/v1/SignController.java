package com.rest.api.controller.v1;

import java.util.Collections;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rest.api.advice.exception.CEmailSigninFailedException;
import com.rest.api.config.security.JwtTokenProvider;
import com.rest.api.domain.entity.UserEntity;
import com.rest.api.domain.repository.UserRepository;
import com.rest.api.domain.response.CommonResult;
import com.rest.api.domain.response.SingleResult;
import com.rest.api.service.ResponseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = {"1. Sign"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1")
public class SignController {
	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final ResponseService responseService;
	private final PasswordEncoder passwordEncoder;
	
	
	@ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
	@PostMapping(value = "/signin")
	public SingleResult<String> signin(@ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String uid,
			@ApiParam(value = "비밀번호", required = true) @RequestParam  String password)
	{
		UserEntity userEntity = userRepository.findByUid(uid).orElseThrow(CEmailSigninFailedException::new);
		if(!passwordEncoder.matches(password, userEntity.getPassword())) {
			throw new CEmailSigninFailedException();
		}
		//로그인시 토큰발행
		return responseService.getSingleResult(jwtTokenProvider.createToken(userEntity.getUsername(), userEntity.getRoles()));
	}
	
	@ApiOperation(value = "회원가입", notes = "회원가입을 한다.")
	@PostMapping(value = "/signup")
	public CommonResult signin(
			@ApiParam(value="회원 ID : 이메일", required = true) @RequestParam String uid,
			@ApiParam(value = "비밀번호", required = true) @RequestParam  String password,
			@ApiParam(value = "이름", required = true) @RequestParam  String name
			)
	{
		userRepository.save(UserEntity.builder()
				.uid(uid)
				.password(passwordEncoder.encode(password))
				.name(name)
				.roles(Collections.singletonList("ROLE_USER")).build());
		return responseService.getSuccessResult();
	}
}
