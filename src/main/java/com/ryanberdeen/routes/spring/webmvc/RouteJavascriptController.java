package com.ryanberdeen.routes.spring.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.LastModified;

import com.ryanberdeen.routes.Mapping;
import com.ryanberdeen.routes.RouteJavascriptGenerator;

public class RouteJavascriptController implements Controller, LastModified {
	private RouteJavascriptGenerator routeJavascriptGenerator;
	private long lastModified;

	public RouteJavascriptController(Mapping mapping) {
		routeJavascriptGenerator = new RouteJavascriptGenerator(mapping);
		lastModified = System.currentTimeMillis();
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		routeJavascriptGenerator.generate(request, response);
		return null;
	}

	public long getLastModified(HttpServletRequest request) {
		return lastModified;
	}
}
