package com.rest.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rest.api.domain.response.CommonResult;
import com.rest.api.domain.response.ListResult;
import com.rest.api.domain.response.SingleResult;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Service
public class ResponseService {
	
	@AllArgsConstructor
	@Getter
	public enum CommonResponse{
		SUCCESS(0, "성공하셨습니다."),
		FAIL(-1, "실패하였습니다.");
		
		int code;
		String msg;
	}
	
	public <T> SingleResult<T> getSingleResult(T data){
		SingleResult<T> result = new SingleResult<>();
		result.setData(data);
		setSuccessResult(result);
		return result;
	}
	
	public <T> ListResult<T> getListResult(List<T> list){
		ListResult<T> result = new ListResult<>();
		result.setList(list);
		setSuccessResult(result);
		return result;
	}
	
	public CommonResult getSuccessResult() {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }
    // 실패 결과만 처리하는 메소드
    public CommonResult getFailResult(int code, String msg) {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
    // 결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
    private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }
	
}
