package cn.ifengkou.utils;

import cn.ifengkou.model.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/25
 */
public class HttpUtils {
    public static String getRequestUrl(HttpServletRequest request){
        //当前请求路径
        String currentUrl = request.getRequestURL().toString();
        //请求参数
        String queryString = request.getQueryString();
        if(!StringUtils.isEmpty(queryString)){
            //currentUrl = currentUrl + "?" + StringEscapeUtils.escapeHtml4(queryString);
            currentUrl += ("?" +queryString);
        }

        String result = "";
        try {
            result = URLEncoder.encode(currentUrl,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            //ignore
        }

        return result;
    }

    public static ResponseEntity<Object> buildJsonResponse(Object data) {
        return buildJsonResponse(null, HttpStatus.OK, "OK", data);
    }

    public static ResponseEntity<Object> buildJsonResponse(String msg, Object data) {
        return buildJsonResponse(null, HttpStatus.OK, msg, data);
    }

    public static ResponseEntity<Object> buildJsonResponse(HttpStatus httpStatus, Object data) {
        return buildJsonResponse(null, httpStatus, httpStatus.getReasonPhrase(), data);
    }
    public static ResponseEntity<Object> buildJsonResponse(HttpStatus httpStatus,String msg, Object data) {
        return buildJsonResponse(null, httpStatus, msg, data);
    }


    public static ResponseEntity<Object> buildJsonResponse(HttpServletRequest request,String msg,Object data) {
        return buildJsonResponse(request, HttpStatus.OK, msg, data);
    }

    public static ResponseEntity<Object> buildJsonResponse(HttpServletRequest request, HttpStatus httpStatus, String msg) {
        return buildJsonResponse(request, httpStatus, msg, null);
    }

    public static ResponseEntity<Object> buildJsonResponse(HttpServletRequest request, HttpStatus httpStatus, String msg, Object data) {
        ResponseResult<Object> result = ResponseResult.builder()
                .msg(msg)
                .timestamp(System.currentTimeMillis())
                .code(httpStatus.value())
                .data(data)
                .path(request!=null?request.getRequestURL().toString():null).build();
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(result, headers, httpStatus);
    }
}
