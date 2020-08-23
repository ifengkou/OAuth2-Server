package cn.ifengkou.config;

import cn.ifengkou.model.exception.ParamsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/23
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ParamsException.class)
    @ResponseBody
    public ResponseEntity<Object> handleParamsException(HttpServletRequest request, ParamsException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> responseResult = new HashMap<>(16);
        responseResult.put("code", httpStatus.value());
        responseResult.put("error", httpStatus.getReasonPhrase());
        responseResult.put("timestamp", new Date());
        responseResult.put("msg", ex.getDetails());
        responseResult.put("path", request.getRequestURL());
        return new ResponseEntity<>(responseResult, headers, httpStatus);
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseEntity<Object> handleException(Exception ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        logRequest(ex, httpStatus, request);
        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> responseResult = new HashMap<>(16);
        responseResult.put("code", httpStatus.value());
        responseResult.put("error", httpStatus.getReasonPhrase());
        responseResult.put("timestamp", new Date());
        responseResult.put("msg", ex.getMessage());
        responseResult.put("path", request.getRequestURL());
        return new ResponseEntity<>(responseResult, headers, httpStatus);
    }



    /**
     * 记录下请求内容
     *
     * @param ex
     * @param status
     * @param request
     */
    private void logRequest(Exception ex, HttpStatus status, HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String uri = request.getRequestURI();
            log.error("User Agent =" + request.getHeader("User-Agent") +
                    ";\nstatus =" + status.toString() + ",reason " + status.getReasonPhrase() +
                    ";\nexception =" + ex.getMessage() +
                    ";\nuri =" + uri +
                    ";\ncontent Type =" + request.getHeader("content-type") +
                    ";\nrequest parameters =" + objectMapper.writeValueAsString(parameters), ex);
        } catch (Exception e) {
            log.error("ControllerAdvice log  Exception", e);
        }
    }


}
