package net.latroquette.common.database.data.file;

import java.io.InputStream;

import javax.servlet.http.Part;

import net.latroquette.common.database.data.profile.User;
import net.latroquette.common.util.parameters.ParameterName;

import org.apache.myfaces.custom.fileupload.UploadedFile;

import com.adi3000.common.database.hibernate.session.DAO;

public interface FilesService extends DAO<File>{
	
	/**
	 * Upload a new picture and resize it to 
	 * the {@code IMG_MAX_WIDTH} and {@code IMG_MAX_HEIGHT} max resolution
	 * @param newFile
	 * @param user
	 * @return a {@link File} registered in database
	 */
	public File uploadNewPicFile(UploadedFile newFile, User user);
	/**
	 * Upload a new picture and resize it to 
	 * the {@code IMG_MAX_WIDTH} and {@code IMG_MAX_HEIGHT} max resolution
	 * @param newFile
	 * @param user
	 * @return a {@link File} registered in database
	 */
	public File uploadNewPicFile(Part newFile, User user);
	
	/**
	 * Delete a file in repository and database
	 * @param file
	 * @return
	 */
	public boolean removeFile(File file);
	/**
	 * Retrieve a file by its database ID
	 * @param id
	 * @return
	 */
	public File getFileById(Integer id);
	
	/**
	 * Compute the physical path of a {@link File} name with the  {@link ParameterName}.DATA_DIR_PATH
	 * @param pathFile
	 * @return
	 */
	public String getPath(String  pathFile);
	
	/**
	 * Modify information or insert a {@link File}  
	 * @param file
	 * @param user
	 * @return
	 */
	public File modifyFile(File file, User user);
	
	/**
	 * Resize an image if needed to {@code imgMaxWidth} and {@code imgMaxHeight} max resolution
	 * @param fisOriginalImage
	 * @param imgMaxWidth
	 * @param imgMaxHeight
	 * @param format
	 * @param fileResized
	 * @return
	 */
    public java.io.File resizeImage(InputStream fisOriginalImage, int imgMaxWidth, int imgMaxHeight, String format, java.io.File fileResized);
}
