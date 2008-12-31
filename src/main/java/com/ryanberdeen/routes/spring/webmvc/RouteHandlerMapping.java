package com.ryanberdeen.routes.spring.webmvc;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.ry1.springframework.web.util.ExtendedParameters.Strategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.util.UrlPathHelper;

import com.ryanberdeen.routes.Mapping;
import com.ryanberdeen.routes.Route;
import com.ryanberdeen.routes.RouteSet;
import com.ryanberdeen.routes.RouteUtils;
import com.ryanberdeen.routes.UrlMatch;

public class RouteHandlerMapping extends AbstractHandlerMapping implements InitializingBean {
	private static final org.springframework.web.util.UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();

	private RouteSet routeSet;

	private String controllerParameterName = "controller";
	private String controllerNameSuffix = "Controller";

	private HashMap<Route, Object> specialRoutes;

	private String javascriptRouteName = "routes.js";
	private String javascriptRouteUri = "/routes.js";
	private Route javascriptRoute;

	public RouteHandlerMapping() {
		routeSet = new RouteSet();
		specialRoutes = new HashMap<Route, Object>();
		javascriptRoute = new Route(javascriptRouteUri, Mapping.EMPTY_PARAMETERS, Mapping.EMPTY_PARAMETERS);
		javascriptRoute.setName(javascriptRouteName);
		
		specialRoutes.put(javascriptRoute, new RouteJavascriptController(routeSet));
	}

	public void setRoutes(List<Route> routes) {
		routeSet.setRoutes(routes);
		routeSet.addRoute(javascriptRoute);
	}

	public void setControllerParameterName(String controllerParameterName) {
		this.controllerParameterName = controllerParameterName;
	}

	public void setControllerNameSuffix(String controllerNameSuffix) {
		this.controllerNameSuffix = controllerNameSuffix;
	}

	public void setContextParameterNames(Set<String> contextParameterNames) {
		routeSet.setContextParameterNames(contextParameterNames);
	}

	public void setStrategy(Strategy strategy) {
		routeSet.setStrategy(strategy);
	}

	@Override
	protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
		String url = URL_PATH_HELPER.getPathWithinApplication(request);

		UrlMatch match = routeSet.getBestMatch(request, url);
		if (match != null) {
			routeSet.setCurrentMatch(request, match);
			Object controller = specialRoutes.get(match.getRoute());

			if (controller == null) {
				String controllerName = match.getParameters().get(controllerParameterName);

				if (controllerName != null) {
					return controllerName + controllerNameSuffix;
				}
			}

			return controller;
		}

		return null;
	}

	public void afterPropertiesSet() throws Exception {
		routeSet.prepare();
		RouteUtils.setMapping(getWebApplicationContext().getServletContext(), routeSet);
	}
}
