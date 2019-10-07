package org.openjava.boot.conf.aop;

import java.util.List;

import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ParamsExceptionHandler {
    /**
         * 直接写在参数列表的参数校验失败处理
     * @param ex
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse> notValid(MethodArgumentNotValidException ex){
        BindingResult result = ex.getBindingResult();
        List<FieldError> errors = result.getFieldErrors();
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError:errors){
        	if(sb.length() > 0) {
				sb.append("\r\n");
			}
            String code = fieldError.getCode();
            if("typeMismatch".equals(code)){
                String name = fieldError.getField();
                String type = result.getFieldType(name).getSimpleName();
                sb.append("param ").append(name).append(" expect ").append(type).append(" value").append(";");
            }else{
                sb.append("["+fieldError.getField()+"]"+fieldError.getDefaultMessage()).append(";");
            }
        }
        return ResponseEntity.badRequest().body(new BasicApiResponse(20032,sb.toString()));
    }

    /**
         * 封装在Javabean的参数校验处理
     * @param ex
     * @return
     */
    @ExceptionHandler({BindException.class})
    public ResponseEntity<ApiResponse> requestMissingServletRequest(BindException ex){
        System.out.println("BindException");
        BindingResult result = ex.getBindingResult();
        List<FieldError> errors = result.getFieldErrors();
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError:errors){
        	if(sb.length() > 0) {
				sb.append("\r\n");
			}
            String code = fieldError.getCode();
            if("typeMismatch".equals(code)){
                String name = fieldError.getField();
                String type = result.getFieldType(name).getSimpleName();
                sb.append("param ").append(name).append(" expect ").append(type).append(" value").append(";");
            }else{
                sb.append("["+fieldError.getField()+"]"+fieldError.getDefaultMessage()).append(";");
            }
        }
        return ResponseEntity.badRequest().body(new BasicApiResponse(20032,sb.toString()));
    }

}
