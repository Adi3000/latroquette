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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import net.latroquette.common.database.IDatabaseConstants;
import net.latroquette.common.database.data.AbstractDAO;
import net.latroquette.common.database.data.profile.User;
import net.latroquette.common.database.session.DatabaseSession;
import net.latroquette.common.security.Security;
import net.latroquette.common.util.CommonUtils;
import net.latroquette.common.util.parameters.ParameterName;
import net.latroquette.common.util.parameters.Parameters;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;

public class Files extends AbstractDAO {
	private static final Logger LOGGER = Logger.getLogger(Files.class.getName());
	private static final String MDSUM_ALGORITHM = "MD5";
	
	public Files(){
		super();
	}
	
	public Files(DatabaseSession db){
		super(db);
	}
	
	/**
	 * Upload and resize a new picture 
	 * @param newFile
	 * @param user
	 * @return
	 */
	public File uploadNewPicFile(UploadedFile newFile, User user){
		if(newFile == null){
			throw new IllegalArgumentException("No file specified");
		}
		Security.checkUserLogged(user);
		//Initialize Message digest
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance(MDSUM_ALGORITHM);
		} catch (NoSuchAlgorithmException e1) {
			LOGGER.log(Level.SEVERE, "Can't find "+MDSUM_ALGORITHM+" algorithm".concat(newFile.getName()), e1);
			return null;
		}
		
        String prefix = String.valueOf(new Date().getTime()).concat(FilenameUtils.getBaseName(newFile.getName()));
        String suffix = FilenameUtils.getExtension(newFile.getName());
        java.io.File resizedFile = null;
        OutputStream output = null;
        InputStream md5Is = null;
        InputStream fis = null;
        Parameters parameters = new Parameters(this);
        java.io.File dataDir = new java.io.File(parameters.getStringValue(ParameterName.DATA_DIR_PATH));

        File file = null;
        try {
        	// Create file with unique name in upload folder and write to it.
        	resizedFile = java.io.File.createTempFile(prefix + "_", "." + suffix, dataDir);
            
            //Create a resized image
        	fis = newFile.getInputStream();
    		ImageIO.write(resizeImage(ImageIO.read(fis)), suffix, resizedFile);
    		fis.close();
    		
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
			LOGGER.log(Level.SEVERE, "Error while processing file : ".concat(newFile.getName()), e);
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
	
	public boolean removeFile(File file){
		boolean success = false;
		if(file.getFile() == null){
			file.setFile(new java.io.File(getPath(file)));
		}
		
		if(file.getFile().delete()){
			file.setDatabaseOperation(IDatabaseConstants.DELETE);
			success = true;
		}else{
			LOGGER.log(Level.WARNING, "Asked for remove but can't delete "+file.getFile().getPath());
			file.setGarbageStatus(GarbageFileStatus.ERROR_ON_DELETE);
			file.setDatabaseOperation(IDatabaseConstants.UPDATE);
			success = false;
		}
		persist(file);
		if(!commit()){
			return false;
		}else{
			return success;
		}
		
	}
	
	public File getFileById(Integer id){
		File file = (File)this.getDataObjectById(id, File.class);
		if(file == null){
			return null;
		}
		//Retrive java.io.File
        file.setFile(new java.io.File(getPath(file)));
    	return file;
	}
	
	private String getPath(File file){
    	Parameters parameters = new Parameters(this);
        return	parameters.getStringValue(ParameterName.DATA_DIR_PATH)
	        		.concat(java.io.File.separator)
	        		.concat(file.getName());
	}
	
	/**
	 * Modify information or insert a {@link File}  
	 * @param file
	 * @param user
	 * @return
	 */
	public File modifyFile(File file, User user){
		file.setOwner(user);
		if(file.getId() == null){
			file.setUploadDate(CommonUtils.getTimestamp());
			file.setDatabaseOperation(IDatabaseConstants.INSERT);
		}else{
			file.setDatabaseOperation(IDatabaseConstants.UPDATE);
		}
		persist(file);
		if(!commit()){
			return null;
		}else{
			return file;
		}
	}
	
    private BufferedImage resizeImage(BufferedImage originalImage){
    	Parameters parameters = new Parameters(this);

    	int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
		int finalHeight = originalImage.getHeight();
		int finalWidth = originalImage.getWidth();
		int imgMaxHeight = parameters.getIntValue(ParameterName.IMG_MAX_HEIGHT);
		int imgMaxWidth = parameters.getIntValue(ParameterName.IMG_MAX_WIDTH);
		
		//Resize keeping the ratio.
		if(finalHeight > imgMaxHeight){
			finalWidth = (int) Math.ceil( finalWidth * (imgMaxHeight / (double)finalHeight) );
			finalHeight = imgMaxHeight;
		}
		if(finalWidth > imgMaxWidth){
			finalHeight = (int) Math.ceil(finalHeight * (imgMaxWidth / (double)finalWidth));
			finalWidth = imgMaxWidth;
		}
		
		BufferedImage resizedImage = new BufferedImage(finalWidth, finalHeight, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, finalWidth, finalHeight, null);
		g.dispose();
		
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
		return resizedImage;
    }
}
