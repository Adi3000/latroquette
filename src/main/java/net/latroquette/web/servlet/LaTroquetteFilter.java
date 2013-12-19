package net.latroquette.web.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LaTroquetteFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(LaTroquetteFilter.class);
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
	    try {
	        chain.doFilter(request, response);
	    } catch (ServletException e) {
	    	logger.error("__FATAL__  :", e);
	        Throwable rootCause = e.getRootCause();
	        if (rootCause instanceof RuntimeException) { // This is true for any FacesException.
	            throw (RuntimeException) rootCause; // Throw wrapped RuntimeException instead of ServletException.
	        } else {
	            throw e;
	        }
	    } catch (IOException e){
	    	logger.error("__FATAL__  :", e);
	    	throw e;
	    }
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.debug("Initialisation of " + LaTroquetteFilter.class.getName());
		
	}

	@Override
	public void destroy() {
		logger.debug("Destroying " + LaTroquetteFilter.class.getName());
	}
}
