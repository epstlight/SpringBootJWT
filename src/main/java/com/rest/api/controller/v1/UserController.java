package com.rest.api.controller.v1;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rest.api.advice.exception.CUserNotFoundException;
import com.rest.api.domain.entity.UserEntity;
import com.rest.api.domain.repository.UserRepository;
import com.rest.api.domain.response.CommonResult;
import com.rest.api.domain.response.ListResult;
import com.rest.api.domain.response.SingleResult;
import com.rest.api.service.ResponseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = {"1. User"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class UserController {
	private final UserRepository userRepository;
	private final ResponseService responseService;
	
	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "회원조회", notes = "모든 회원을 조회한다.")
	@GetMapping(value = "/users")
	public ListResult<UserEntity> findAllUser(){
		return responseService.getListResult(userRepository.findAll());
	}
	
	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "회원등록", notes = "회원을 입력한다.")
	@PostMapping(value = "/user")
	public SingleResult<UserEntity> save(@ApiParam(value = "아이디", required = true) @RequestParam String uid,
			@ApiParam(value = "회원이름", required = true) @RequestParam String name) {
		UserEntity userEntity = UserEntity.builder()
				.uid(uid)
				.name(name)
				.build();
		return responseService.getSingleResult(userRepository.save(userEntity));
	}
	
	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "회원 단일 조회", notes = "userId로 회원을 조회")
	@GetMapping(value = "/user")
	public SingleResult<UserEntity> findUserById(
			@ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang
			){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
		return responseService.getSingleResult(userRepository.findByUid(id).orElseThrow(CUserNotFoundException::new));
	}
	
	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "회원수정", notes = "회원정보를 수정")
	@PutMapping(value = "/user")
	public SingleResult<UserEntity> modify(
			@ApiParam(value = "회원번호", required = true) @RequestParam long msrl,
			@ApiParam(value = "회원아이디", required = true) @RequestParam String uid,
			@ApiParam(value = "회원이름", required = true) @RequestParam String name
			){
		UserEntity userEntity = UserEntity.builder()
				.msrl(msrl)
				.uid(uid)
				.name(name)
				.build();
		return responseService.getSingleResult(userRepository.save(userEntity));
	}
	
	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "회원삭제", notes="회원정보 삭제")
	@DeleteMapping(value = "/user/{msrl}")
	public CommonResult delete(
			@ApiParam(value = "회원 번호") @PathVariable("msrl") long msrl) {
		userRepository.deleteById(msrl);
		return responseService.getSuccessResult();
	}
	
}
