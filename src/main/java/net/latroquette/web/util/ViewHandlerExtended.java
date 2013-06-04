package net.latroquette.web.util;

import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

public class ViewHandlerExtended extends ViewHandlerWrapper{
    private ViewHandler parent;

    public ViewHandlerExtended(ViewHandler parent) {
        this.parent = parent;
    }

    @Override
    public ViewHandler getWrapped() {
        return parent;
    }
	public String getActionURL(FacesContext context, String viewId) {
		HttpServletRequest request = (HttpServletRequest) 
	            context.getExternalContext().getRequest();
	    // remaining on the same view keeps URL state 
	    String requestViewID = request.getRequestURI().substring(
	            request.getContextPath().length());
	    if (requestViewID.equals(viewId)) {

	        // keep RESTful URLs and query strings
	        String action = (String) request.getAttribute(
	                RequestDispatcher.FORWARD_REQUEST_URI);
	        if (action == null) {
	            action = request.getRequestURI();
	        }
	        if (request.getQueryString() != null) {
	            return action + "?" + request.getQueryString();
	        } else {
	            return action;
	        }
	    } else {
	        // moving to a new view drops old URL state 
	        return super.getActionURL(context, viewId);
	    }
	}

}
