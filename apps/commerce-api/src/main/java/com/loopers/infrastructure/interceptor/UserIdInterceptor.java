package com.loopers.infrastructure.interceptor;

import com.loopers.support.RequireUserId;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class UserIdInterceptor implements HandlerInterceptor {

	private static final String USER_ID_HEADER = "X-USER-ID";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (handler instanceof HandlerMethod handlerMethod && handlerMethod.hasMethodAnnotation(RequireUserId.class)) {
			String userId = request.getHeader(USER_ID_HEADER);
			if (userId == null) {
				throw new CoreException(ErrorType.BAD_REQUEST, "X-USER-ID 헤더가 없습니다.");
			}
		}
		return true;
	}
}
