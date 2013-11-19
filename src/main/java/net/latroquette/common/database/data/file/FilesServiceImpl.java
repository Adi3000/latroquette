package net.latroquette.common.database.data.file;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.Part;

import net.latroquette.common.database.data.profile.User;
import net.latroquette.common.util.Services;
import net.latroquette.common.util.parameters.ParameterName;
import net.latroquette.common.util.parameters.Parameters;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.adi3000.common.database.hibernate.DatabaseOperation;
import com.adi3000.common.database.hibernate.session.AbstractDAO;
import com.adi3000.common.util.CommonUtil;
import com.adi3000.common.util.security.Security;

@Repository(value=Services.FILES_SERVICE)
public class FilesServiceImpl extends AbstractDAO<File> implements FilesService{

	private static final Logger LOGGER = LoggerFactory.getLogger(FilesServiceImpl.class.getName());
	private static final String MDSUM_ALGORITHM = "MD5";
	
	@Autowired
	private transient Parameters parameters;
	
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	public FilesServiceImpl(){
		super();
	}
	
	@Transactional(readOnly=false)
	public File uploadNewPicFile(UploadedFile newFile, User user){
		InputStream newFileInputStream = null;
		File file = null;
		try {
			newFileInputStream = newFile.getInputStream();
			file = uploadNewPicFile(newFile.getName(), newFileInputStream, user);
		} catch (IOException e) {
			LOGGER.error( "Error while processing file : ".concat(newFile.getName()), e);
			file = null;
		}
		return file;
	}
	
	@Transactional(readOnly=false)
	public File uploadNewPicFile(Part newFile, User user){
		InputStream newFileInputStream = null;
		File file = null;
		try {
			newFileInputStream = newFile.getInputStream();
			file = uploadNewPicFile(newFile.getName(), newFileInputStream, user);
		} catch (IOException e) {
			LOGGER.error( "Error while processing file : ".concat(newFile.getName()), e);
			file = null;
		}
		return file;
	}
	/**
	 * Upload and resize a new picture with the {@link ParameterName}.IMG_MAX_HEIGHT
	 * and {@ParameterName.IMG_MAX_WIDTH} max size
	 * @param newFile
	 * @param user
	 * @return
	 */
	@Transactional(readOnly=false)
	public File uploadNewPicFile(String fileName, InputStream newFileInputStream, User user){
		Security.checkUserLogged(user);
		//Initialize Message digest
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance(MDSUM_ALGORITHM);
		} catch (NoSuchAlgorithmException e1) {
			LOGGER.error("Can't find "+MDSUM_ALGORITHM+" algorithm for ".concat(fileName), e1);
			return null;
		}
		fileName = fileName.replaceAll("[^\\p{ASCII}]", "");
        String prefix = String.valueOf(new Date().getTime()).concat("_").concat(FilenameUtils.getBaseName(fileName));
        String suffix = FilenameUtils.getExtension(fileName);
        java.io.File resizedFile = null;
        OutputStream output = null;
        InputStream md5Is = null;
        InputStream fis = null;
        java.io.File dataDir = new java.io.File(parameters.getStringValue(ParameterName.DATA_DIR_PATH));
        File file = null;
        try {
        	// Create file with unique name in upload folder and write to it.
        	resizedFile = java.io.File.createTempFile(prefix.concat("_"), ".".concat(suffix), dataDir);
            
            //Create a resized image
    		int imgMaxHeight = parameters.getIntValue(ParameterName.IMG_MAX_HEIGHT);
    		int imgMaxWidth = parameters.getIntValue(ParameterName.IMG_MAX_WIDTH);
    		resizedFile = resizeImage(newFileInputStream, imgMaxWidth, imgMaxHeight, suffix, resizedFile);
    		
    		//Watch for checksum
    		fis = new FileInputStream(resizedFile);
    		md5Is = new DigestInputStream(fis, messageDigest);
    		while(md5Is.read() >= 0);
    		//Extract MessageDigest
    		String md5Sum = new BigInteger(1,messageDigest.digest()).toString(16);
			md5Is.close();
			
			//Construct File object
			file = new File();
			file.setFile(resizedFile);
	        file.setChecksum(md5Sum);
	        file.setOwner(user);
	        file.setName(resizedFile.getName());
	        file.setGarbageStatus(GarbageFileStatus.NOT_LINKED);
        } catch (IOException e) {
			LOGGER.error( "Error while processing file : ".concat(fileName), e);
			file = null;
			// Cleanup.
			if (resizedFile != null){
				resizedFile.delete();
			}
        } finally {
        	IOUtils.closeQuietly(md5Is);
        	IOUtils.closeQuietly(fis);
            IOUtils.closeQuietly(output);
        }
        //Registrer file in database
        if(file != null){
        	file = modifyFile(file, user);
        }
        return file;
	}
	
	/**
	 * Delete a file in repository and database
	 * @param file
	 * @return
	 */
	@Transactional(readOnly=false)
	public boolean removeFile(File file){
		boolean success = false;
		if(file.getFile() == null){
			file.setFile(new java.io.File(getPath(file)));
		}
		
		if(file.getFile().delete()){
			file.setDatabaseOperation(DatabaseOperation.DELETE);
			success = true;
		}else{
			LOGGER.warn("Asked for remove but can't delete "+file.getFile().getPath());
			file.setGarbageStatus(GarbageFileStatus.ERROR_ON_DELETE);
			file.setDatabaseOperation(DatabaseOperation.UPDATE);
			success = false;
		}
		persist(file);
		return success;
		
	}
	
	/**
	 * Retrieve a file by its database ID
	 * @param id
	 * @return
	 */
	@Transactional(readOnly=true)
	public File getFileById(Integer id){
		File file = this.getDataObjectById(id, File.class);
		if(file == null){
			return null;
		}
		//Retrieve java.io.File
        file.setFile(new java.io.File(getPath(file)));
    	return file;
	}
	
	private String getPath(File file){
		return getPath(file.getName());
	}
	
	/**
	 * Compute the physical path of a {@link File} name with the  {@link ParameterName}.DATA_DIR_PATH
	 * @param pathFile
	 * @return
	 */
	public String getPath(String  pathFile){
        String path = parameters.getStringValue(ParameterName.DATA_DIR_PATH)
	        		.concat(java.io.File.separator)
	        		.concat(pathFile);
        return path;
	}
	
	/**
	 * Modify information or insert a {@link File}  
	 * @param file
	 * @param user
	 * @return
	 */	
	@Transactional(readOnly=false)
	public File modifyFile(File file, User user){
		file.setOwner(user);
		if(file.getId() == null){
			file.setUploadDate(CommonUtil.getTimestamp());
		}
		return modifyDataObject(file);
	}
	
	/**
	 * Resize an image if needed to {@code imgMaxWidth} and {@code imgMaxHeight} max resolution
	 * @param fisOriginalImage
	 * @param imgMaxWidth
	 * @param imgMaxHeight
	 * @param format
	 * @param fileResized
	 * @return
	 */
    public java.io.File resizeImage(InputStream fisOriginalImage, int imgMaxWidth, int imgMaxHeight, String format, java.io.File fileResized) {
    	BufferedImage originalImage = null;
    	try {
			originalImage = ImageIO.read(fisOriginalImage);
		} catch (IOException e) {
			LOGGER.error("Can't read file for resize "+fileResized.getName(), e);
			return null;
		}
    	boolean imageModified = false;
    	int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
		int finalHeight = originalImage.getHeight();
		int finalWidth = originalImage.getWidth();
		//Resize keeping the ratio.
		if(finalHeight > imgMaxHeight){
			finalWidth = (int) Math.ceil( finalWidth * (imgMaxHeight / (double)finalHeight) );
			finalHeight = imgMaxHeight;
			imageModified = true;
		}
		if(finalWidth > imgMaxWidth){
			finalHeight = (int) Math.ceil(finalHeight * (imgMaxWidth / (double)finalWidth));
			finalWidth = imgMaxWidth;
			imageModified = true;
		}
		BufferedImage resizedImage = originalImage;
		if(imageModified){
			resizedImage = new BufferedImage(finalWidth, finalHeight, type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, finalWidth, finalHeight, null);
			g.dispose();

			//Comment this if you feel some performance lack 
			g.setComposite(AlphaComposite.Src);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			//End of image processing variable
			
		}
		
		try{
			ImageIO.write(resizedImage, format,  fileResized);
			fisOriginalImage.close();
	    } catch (IOException e) {
	    	LOGGER.error("Can't write file for resize "+fileResized.getName(), e);
	    	return null;
	    }
		return fileResized;
    }
}
