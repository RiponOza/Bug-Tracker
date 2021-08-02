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
import org.mindrot.jbcrypt.BCrypt;
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


	public String encryptPassword(String password) {
		try {
			return BCrypt.hashpw(password, BCrypt.gensalt(10));
		} catch (Exception e) {
			return null;
		}
	}
	
	// validates the user credentials
	public boolean validateUser(String email, String password) {
		String hashedPassword = userDao.getUserPassword(email);
		if(hashedPassword==null) {
			return false;
		}
		if(BCrypt.checkpw(password, hashedPassword)) {
			return true;
		}else {
			return false;
		}
		
	}

	/**
	 * This method performs user registration.
	 * 
	 * @param user This is user object which holds information of a user.
	 * @return true for successful registration and false for failure.
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public boolean registerUser(User user) {
		try {
			// encrypts the user provided password
			String encPassword = encryptPassword(user.getPassword());
			userDao.saveUser(user);
			userDao.saveLoginDetail(user.getEmail(), encPassword);
			return true;
		}catch (Exception e) {
			return false;
		}
	}

	/**
	 * This method gives user details.
	 * 
	 * @param email This is user provided email.
	 * @return user object.
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public User getUser(String id) {
		return userDao.getUser(id);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public User getUserByEmail(String email) {
		return userDao.getUserByEmail(email);
	}

	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public boolean updateUser(User user) {
		return userDao.updateUser(user);
	}

	public boolean isEmailAvailable(String email) {
		return userDao.isEmailAvailable(email);
	}
	

	public boolean updatePassword(String email, String password) {
		String encPassword = encryptPassword(password);
		return userDao.updatePassword(email, encPassword);
	}

	@Transactional(isolation = Isolation.REPEATABLE_READ)
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

	// removes profile pic
	public boolean deleteProfilePic(String userid) {
		return userDao.deleteProfilePic(userid);
	}
}
