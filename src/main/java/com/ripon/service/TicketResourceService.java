package com.ripon.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ripon.dao.TicketResourceDao;
import com.ripon.entity.TicketResource;

@Service
public class TicketResourceService {
	
	@Value("${file-upload-path}")
	private String fileUploadPath;
	@Autowired
	private TicketResourceDao ticketResourceDao;
	
	// uploads ticket resource
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public boolean uploadTicketResource(String ticketId, MultipartFile[] files) {
		try {
			String path = fileUploadPath + "Tickets/" + ticketId + "/";
			File f = new File(path);
			if (!f.exists()) {
				f.mkdirs();
			}
			FileOutputStream fout = null;
			for (MultipartFile file : files) {
				String extension = FilenameUtils.getExtension(file.getOriginalFilename());
				if(extension.equals("pdf") || extension.equals("txt") || extension.equals("docx") || extension.equals("ppt") || extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png")) {
					String filename = file.getOriginalFilename(); //RandomStringUtils.randomAlphanumeric(10) + "." + extension;					
					fout = new FileOutputStream(new File(path + filename));
					fout.write(file.getBytes());
					// save file details in db
					TicketResource resource = new TicketResource();
					resource.setticketId(ticketId);
					resource.setResourceName(filename);
					resource.setResourceType(extension);
					Date date = new Date(System.currentTimeMillis());
					resource.setCreated(date.toString());
					//System.out.println(resource);
					ticketResourceDao.saveTicketResourceDetail(resource);
				}
			}
			fout.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public byte[] showTicketResource(String ticketId, String resourceName) {
		try {
			String path = fileUploadPath + "/Tickets/" + ticketId + "/" + resourceName;
			FileInputStream fin = new FileInputStream(new File(path));
			return IOUtils.toByteArray(fin);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// get resources of a ticket 
	public List<TicketResource> getTicketResources(String ticketId){
		return ticketResourceDao.getTicketResources(ticketId);
	}
	
	

	public boolean deleteTicketResource(String resourceId, String ticketId, String resourceName) {
		String path = fileUploadPath+"/"+"Tickets" + "/" + ticketId + "/" + resourceName;
		File file = new File(path);
		if(file.exists()) {
			file.delete();
		}
		return ticketResourceDao.deleteTicketResource(resourceId);
	}
	
	// delete all the resources of a ticket and the ticket folder
	public boolean deleteTicketResources(String ticketId) {
		try {
			String path = fileUploadPath + "/Tickets/"+ticketId+"";
			File file = new File(path);
			if(file.list().length==0) {
				file.delete();
			}else {
				File[] files = file.listFiles();
				for(File f : files) {
					f.delete();
				}
			}
			file.delete();
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
