package com.example.User.resolver;

import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

public class MasterIdArgumentResolver  implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasMasterIdAnnotation = parameter.hasParameterAnnotation(MasterId.class);
        boolean hasIntegerType = Integer.class.isAssignableFrom(parameter.getParameterType());
        return hasMasterIdAnnotation && hasIntegerType;
    }

    @Override
    public Integer resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer
            , NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        return Optional.ofNullable(webRequest.getNativeRequest(HttpServletRequest.class))
                .map(request -> request.getHeader("id"))
                .filter(header -> !header.trim().isEmpty())
                .map(this::parseIdSafely)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REQUEST));
    }

    private Integer parseIdSafely(String header) {
        try {
            int id = Integer.parseInt(header.trim());
            if (id <= 0) {  // ID 유효성 검사 추가
                throw new CustomException(ErrorCode.INVALID_NUMBER_FORMAT);
            }
            return id;
        } catch (NumberFormatException e) {
            throw new CustomException(ErrorCode.INVALID_NUMBER_FORMAT);
        }
    }
}
