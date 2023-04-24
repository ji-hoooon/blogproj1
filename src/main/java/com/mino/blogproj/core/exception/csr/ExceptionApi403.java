package com.mino.blogproj.core.exception.csr;


import com.mino.blogproj.dto.ResponseDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 권한 없음
@Getter
public class ExceptionApi403 extends RuntimeException {
    public ExceptionApi403(String message) {
        super(message);
    }

    public ResponseDTO<?> body(){
        ResponseDTO<String> responseDto = new ResponseDTO<>();
        responseDto.fail(HttpStatus.FORBIDDEN, "forbidden", getMessage());
        return responseDto;
    }

    public HttpStatus status(){
        return HttpStatus.FORBIDDEN;
    }
}