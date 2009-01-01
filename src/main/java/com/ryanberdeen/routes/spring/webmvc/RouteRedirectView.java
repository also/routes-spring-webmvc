package com.ryanberdeen.routes.spring.webmvc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.ryanberdeen.routes.RouteUtils;

public class RouteRedirectView implements View {
	private HashMap<String, Object> parameters = new HashMap<String, Object>();

	public RouteRedirectView(Object... parameterArray) {
		if (parameterArray.length % 2 != 0) {
			throw new IllegalArgumentException("Parameters must have an even number of elements");
		}

		for (int i = 0; i < parameterArray.length; i += 2) {
			parameters.put((String) parameterArray[i], parameterArray[i + 1]);
		}
	}

	public String getContentType() {
		return null;
	}

	@SuppressWarnings("unchecked")
	public void render(Map model, HttpServletRequest request, HttpServletResponse response) {
		ServletContext servletContext = RequestContextUtils.getWebApplicationContext(request).getServletContext();
		String url = RouteUtils.getMapping(servletContext).getPath(request, parameters, true);

		response.setStatus(303);
		response.setHeader("Location", response.encodeRedirectURL(url));
	}
}
