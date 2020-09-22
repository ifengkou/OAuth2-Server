package cn.ifengkou.config;

import cn.ifengkou.model.ResponseResult;
import cn.ifengkou.model.exception.AlreadyExistsException;
import cn.ifengkou.model.exception.BusinessException;
import cn.ifengkou.model.exception.ParamsException;
import cn.ifengkou.utils.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
        return HttpUtils.buildJsonResponse(request, HttpStatus.BAD_REQUEST, "参数校验异常", ex.getDetails());
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    @ResponseBody
    public ResponseEntity<Object> handleAlreadyExistsException(HttpServletRequest request, AlreadyExistsException ex) {
        return HttpUtils.buildJsonResponse(request, HttpStatus.NOT_IMPLEMENTED, ex.getMessage() == null ? "对象已存在" : ex.getMessage());
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    @ResponseBody
    public ResponseEntity<Object> handleEntityNotFoundException(HttpServletRequest request, EntityNotFoundException ex) {
        return HttpUtils.buildJsonResponse(request, HttpStatus.NOT_IMPLEMENTED, ex.getMessage() == null ? "对象不存在" : ex.getMessage());
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResponseEntity<Object> handleBusinessException(HttpServletRequest request, BusinessException ex) {
        return HttpUtils.buildJsonResponse(request, HttpStatus.NOT_IMPLEMENTED, ex.getMessage() == null ? "业务处理异常" : ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Object> handleValidationExceptions(HttpServletRequest request, MethodArgumentNotValidException ex) throws IOException {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error("spring参数校验异常", ex);
        return HttpUtils.buildJsonResponse(request, HttpStatus.BAD_REQUEST, "参数校验异常", errors);
    }



    @ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseEntity<Object> handleException(Exception ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        logRequest(ex, httpStatus, request);
        return HttpUtils.buildJsonResponse(request, httpStatus, StringUtils.isBlank(ex.getMessage()) ? httpStatus.getReasonPhrase() : ex.getMessage());
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
