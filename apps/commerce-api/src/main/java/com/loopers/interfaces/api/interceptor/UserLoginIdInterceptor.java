package com.loopers.interfaces.api.interceptor;

import com.loopers.support.RequireUserLoginId;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class UserLoginIdInterceptor implements HandlerInterceptor {

	private static final String USER_LOGIN_ID_HEADER = "X-USER-ID";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (handler instanceof HandlerMethod handlerMethod && handlerMethod.hasMethodAnnotation(RequireUserLoginId.class)) {
			String loginId = request.getHeader(USER_LOGIN_ID_HEADER);
			if (loginId == null) {
				throw new CoreException(ErrorType.BAD_REQUEST, "X-USER-ID 헤더가 없습니다.");
			}
		}
		return true;
	}
}
