package com.cmarchive.bank.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cmarchive.bank.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cmarchive.bank.component.MyUserDetails;
import com.cmarchive.bank.domain.User;

public class BaseInterceptor extends HandlerInterceptorAdapter {

	private TokenService tokenService;

	@Autowired
	public BaseInterceptor(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof MyUserDetails) {
			User user = ((MyUserDetails) authentication.getPrincipal()).getUser();
			request.getSession().setAttribute("loggedUser", user);
		}
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		String controllerName = "";
		String actionName = "";
		
		if (handler instanceof HandlerMethod && modelAndView != null) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			controllerName = handlerMethod.getBeanType().getSimpleName();
			actionName = handlerMethod.getMethod().getName();
			
			modelAndView.addObject("controllerName", controllerName);
			modelAndView.addObject("actionName", actionName);
		}
	}
}
