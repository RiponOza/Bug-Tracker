/** This class is responsible for all the operations related to user. Here methods 
 * for user registration and login are provided. This class uses userDao object to 
 * perform database operations. 
 * 
 * @author Ripon Oza
 * @version 1.0
 * @since 2021
 */
package com.ripon.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ripon.dao.UserDao;
import com.ripon.entity.Login;
import com.ripon.entity.User;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Value("${profile-image-path}")
	private String profileImagePath;

	/**
	 * This method takes the user provided password and create hash code of length
	 * 16 and returns it.
	 * 
	 * @param passwordToHash
	 * @return encrypted password
	 */
	public String encryptPassword(String passwordToHash) {
		String generatedPassword = null;
		try {
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			// Add password bytes to digest
			md.update(passwordToHash.getBytes());
			// Get the hash's bytes
			byte[] bytes = md.digest();
			// This bytes[] has bytes in decimal format;
			// Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			// Get complete hashed password in hex format
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	/**
	 * This method performs user registration.
	 * 
	 * @param user This is user object which holds information of a user.
	 * @return true for successful registration and false for failure.
	 */
	public boolean registerUser(User user) {
		// encrypts the user provided password
		String encPassword = encryptPassword(user.getPassword());
		// save encrypted password
		user.setPassword(encPassword);
		return userDao.saveUser(user);
	}

	/**
	 * This method checks whether a user is registered one or not.
	 * 
	 * @param email    This is user provided email.
	 * @param password This is user provided password.
	 * @return user object.
	 */
	public User getUser(String id, String password) {
		//System.out.println("inside service");
		// encrypts the user provided password
		String encPassword = encryptPassword(password);
		return userDao.getUser(id, encPassword);
	}

	/**
	 * This method gives user details.
	 * 
	 * @param email This is user provided email.
	 * @return user object.
	 */
	public User getUser(String id) {
		return userDao.getUser(id);
	}
	
	public User getUserByEmail(String email) {
		return userDao.getUserByEmail(email);
	}

	public boolean updateUser(User user) {
		return userDao.updateUser(user);
	}

	public boolean isEmailAvailable(String email) {
		return userDao.isEmailAvailable(email);
	}
	

	public boolean updatePassword(String id, String password) {
		String encPassword = encryptPassword(password);
		return userDao.updatePassword(id, encPassword);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public boolean saveProfilePic(String id, MultipartFile file) throws IOException {
		File f = new File(profileImagePath + id + ".jpg");
		if(!f.exists()) {
			f.createNewFile();
		}
		FileOutputStream fout = new FileOutputStream(f);
		fout.write(file.getBytes());
		fout.close();
		User user = getUser(id);
		user.setImage(id + ".jpg");
		updateUser(user);
		return true;
	}
	
	public File getProfilePic(String imageName) {
		return new File(profileImagePath + imageName);
	}

}
