package net.latroquette.web.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.latroquette.common.database.data.file.File;
import net.latroquette.common.database.data.file.FilesService;
import net.latroquette.common.util.parameters.ParameterName;
import net.latroquette.common.util.parameters.Parameters;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.adi3000.common.web.ServletUtils;

/**
 * The Image servlet for serving from absolute path.
 * @author BalusC
 * @link http://balusc.blogspot.com/2007/04/imageservlet.html
 */
public class ImageServlet extends HttpServlet {

    // Constants ----------------------------------------------------------------------------------
	public static final String BY_PATH = "p";
	public static final String GET_SMALL_SIZE = "s";
    /**
	 * 
	 */
	private static final long serialVersionUID = -9123476715208295532L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageServlet.class.getName());

	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.

    // Actions ------------------------------------------------------------------------------------
	@Inject
	private transient FilesService filesService; 
	@Inject
	private transient Parameters parameters;
		
    /**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}



	/**
	 * @param filesService the filesService to set
	 */
	public void setFilesService(FilesService filesService) {
		this.filesService = filesService;
	}



	protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // Get requested image by path info.
        String requestedImage = request.getPathInfo();
        boolean smallSize = false;
        boolean byPath = false;
        java.io.File image = null;
        // Check if file name is actually supplied to the request URI.
        if (requestedImage == null) {
            // Do your thing if the image is not supplied to the request URI.
            // Throw an exception, or send 404, or show default/warning image, or just ignore it.
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }
        String[] pathRequest = requestedImage.substring(1).split(ServletUtils.HTML_SEPARATOR);
        //Analyse path to check parameters
        for(int i = 0; i < pathRequest.length ; i++){
    		if(GET_SMALL_SIZE.equals(pathRequest[i])){
    			smallSize = true;
    		}else if(BY_PATH.equals(pathRequest[i])){
    			byPath = true;
    		}else{
    			requestedImage = pathRequest[i];
    		}
        }
        if(byPath){
        	image = new java.io.File(filesService.getPath(requestedImage));
        }else{
	        File file = filesService.getFileById(Integer.valueOf(requestedImage)) ;
	        if(file == null){
	            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
	            LOGGER.warn(request.getLocalAddr() + " requested for non existing File id : " + requestedImage );
	            return;
	        }
	        image = file.getFile();
        }

        // Check if file actually exists in filesystem.
        if (!image.exists()) {
            // Throw an exception, or send 404, or show default/warning image, or just ignore it.
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            LOGGER.warn(request.getLocalAddr() + " requested for File id : " + requestedImage +", path :"+ image.getPath());
            return;
        }

        // Get content type by filename.
        String contentType = getServletContext().getMimeType(image.getName());

        // Check if file is actually an image (avoid download of other files by hackers!).
        if (contentType == null || !contentType.startsWith("image")) {
            // Throw an exception, or send 404, or show default/warning image, or just ignore it.
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            LOGGER.warn(request.getLocalAddr() + " requested for non image File id : " + requestedImage +", path :"+ image.getPath());
            return;
        }
        java.io.File outputImage = image;
        
        //Resize image to avoid huge download (maybe useless and more stressfull than deliver non cropped images)
        java.io.File resizedFile = null;
        if(smallSize){
        	//TODO check if a separate folder should be provided for resized image due to massive read and write
        	java.io.File dataDir = new java.io.File(parameters.getStringValue(ParameterName.DATA_DIR_PATH));
        	// Create file with unique name in upload folder and write to it.
        	String suffix = FilenameUtils.getExtension(image.getName());
        	resizedFile = java.io.File.createTempFile("small_", ".".concat(suffix), dataDir);

            //Create a resized image
        	InputStream fis = new FileInputStream(image);
    		int imgMaxHeight = parameters.getIntValue(ParameterName.IMG_SMALL_HEIGHT);
    		int imgMaxWidth = parameters.getIntValue(ParameterName.IMG_SMALL_WIDTH);
    		resizedFile = filesService.resizeImage(fis, imgMaxWidth, imgMaxHeight, suffix, resizedFile);
    		outputImage = resizedFile;
        }

        // Init servlet response.
        response.reset();
        response.setBufferSize(DEFAULT_BUFFER_SIZE);
        response.setContentType(contentType);
        response.setHeader("Content-Length", String.valueOf(outputImage.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + outputImage.getName() + "\"");

        // Prepare streams.
        BufferedInputStream input = null;
        BufferedOutputStream output = null;

        try {
            // Open streams.
            input = new BufferedInputStream(new FileInputStream(outputImage), DEFAULT_BUFFER_SIZE);
            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

            // Write file contents to response.
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            if(resizedFile != null){
            	resizedFile.delete();
            }
        } finally {
            // Gently close streams.
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(input);
        }
    }
	
	@Override
	public void init(ServletConfig config) throws ServletException {
			super.init(config);
			SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
				      config.getServletContext());
	}

}
