package com.ripon.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ripon.entity.Project;
import com.ripon.entity.User;
import com.ripon.service.ProjectService;
import com.ripon.service.UserService;

@Controller
public class ProjectController {

	@Autowired
	private Project project;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private UserService userService;

	@GetMapping("/create-project")
	public String getCreateProject(Model model, HttpSession session) {
		User user = userService.getUser(session.getAttribute("userid").toString());
		if (user.getRole().equals("AD")) {
			model.addAttribute("project", project);
			return "CreateProject";
		} else {
			return "login";
		}
	}

	@PostMapping("/create-project")
	public String createProject(@ModelAttribute("project") Project project, BindingResult result, Model model,
			HttpSession session) {
		project.setAdminId(session.getAttribute("userid").toString());
		boolean status = projectService.saveProject(project);
		if (status) {
			model.addAttribute("status_success", "Project is created successfully !");
			return "CreateProject";
		} else {
			model.addAttribute("status_fail", "Oops, Error occured !");
			return "CreateProject";
		}

	}

	
	
	@GetMapping("/my-projects")
	public String getProjects(Model model, HttpSession session) {
		String userid = session.getAttribute("userid").toString();
		if (userid == null) {
			return "redirect:/login";
		}

		List<Project> projectList = projectService.getProjectsOfAdmin(session.getAttribute("userid").toString());
		if (projectList == null || projectList.isEmpty()) {
			model.addAttribute("projectCount", 0);
			return "show_projects";
		}

		model.addAttribute("projectCount", projectList.size());
		model.addAttribute("projectList", projectList);

		return "show_projects";
	}
	
	
	
	@GetMapping("/get-project-user/{userId}")
	public String getProjectofUser(@PathVariable("userId") String userId, Model model, HttpSession session) {
		User user = userService.getUser(session.getAttribute("userid").toString());
		if(user == null) {
			return "redirect:/login";
		}
		if(!user.getRole().equals("DV") && !user.getRole().equals("TS")) {
			return "redirect:/login";
		}
		Project project = projectService.getProjectOfUser(userId);
		if(project==null) {
			model.addAttribute("projectCount", 0);
			return "";
		}
		return "";
	}
	
	
	
	@GetMapping("/get-project-pm")
	public String getProjectofProjectManager(Model model, HttpSession session) {
		String managerId = session.getAttribute("userid").toString();
		User user = userService.getUser(managerId);
		if(user == null) {
			return "redirect:/login";
		}
		if(!user.getRole().equals("PM")) {
			return "redirect:/login";
		}
		Project project = projectService.getProjectOfManager(managerId);
		if(project==null) {
			model.addAttribute("hasProject", false);
			return "get_project_pm";
		}
		model.addAttribute("hasProject", true);
		model.addAttribute("title", project.getTitle());
		model.addAttribute("desc", project.getDesc());
		model.addAttribute("createdDate", project.getCreatedDate());
		model.addAttribute("createdTime", project.getCreatedTime());
		model.addAttribute("daysPassed", project.getDaysPassed());
		model.addAttribute("desc", project.getDesc());
		//model.addAttribute("projectId", project.getId());
		return "get_project_pm";
	}
	
	
	
	
	@GetMapping("/project-desc/{projectId}")
	public String getProjectDescription(@PathVariable("projectId") String projectId, Model model) {
		Project project = projectService.getProject(projectId);
		model.addAttribute("title", project.getTitle());
		model.addAttribute("desc", project.getDesc());
		model.addAttribute("createdDate", project.getCreatedDate());
		model.addAttribute("createdTime", project.getCreatedTime());
		model.addAttribute("daysPassed", project.getDaysPassed());
		model.addAttribute("desc", project.getDesc());
		model.addAttribute("projectId", project.getId());
		String managerId = project.getManagerId();
		System.out.println(project);
		if(managerId == null) {
			model.addAttribute("projectManager", null);
		}else {
			User projectManager = userService.getUser(managerId);
			model.addAttribute("managerId", projectManager.getPhone());
			model.addAttribute("projectManager", projectManager.getFname() + " " + projectManager.getLname());
			model.addAttribute("email", projectManager.getEmail());
			model.addAttribute("phone", projectManager.getPhone());
			model.addAttribute("image", "/profile-image/" + projectManager.getImage());
		}
		
		return "project_detail";
	}
	
	
	
	
	@PostMapping("/assign-project")
	public String assignProject(@RequestParam("projectId") String projectId, @RequestParam("email") String managerEmail, Model model, HttpSession session) {
		String url = "redirect:/project-desc/" + projectId;
		
		if(session.getAttribute("userid")==null) {
			return "login";
		}
		
		User manager = userService.getUserByEmail(managerEmail);
		if(manager==null) {
			return url;
		}
		
		if(!manager.getRole().toString().equals("PM")) {
			return url;
		}
		
		if(!projectService.isManagerFree(manager.getPhone())) {
			return url;
		}
		
		projectService.assignProject(projectId, manager.getPhone());
		return url;
	}
	
	
	
	
	
	@GetMapping("/unassign-project/{projectId}")
	public String unassignProject(@PathVariable("projectId") String projectId, Model model, HttpSession session) {
		if(session.getAttribute("userid")==null) {
			return "login";
		}
		projectService.unassignProject(projectId);
		String url = "redirect:/project-desc/" + projectId;
		return url;
	}
	
	
	
	@CrossOrigin
	@ResponseBody
	@GetMapping("/validate-manager")
	public Integer validateManager(@RequestParam("email") String managerEmail, HttpSession session) {
		try {
			managerEmail = managerEmail.trim();
			if(session.getAttribute("userid")==null) {
				return 0; //user not logged in
			}
			User user = userService.getUserByEmail(managerEmail);
			System.out.println(user);
			if(user==null) {
				return -1; // manager not available
			}
			if(!user.getRole().toString().equals("PM")) {
				return -2; // user is not manager
			}
			if(!projectService.isManagerFree(user.getPhone())) {
				return -3; // manager not free
			}
			return 1; //everything is fine
		} catch (Exception e) {
			return -100; // error occured
		}
	}
	
	
	
	@GetMapping("/manage-project-user")
	public String addUserToProject(HttpSession session) {
		if(session.getAttribute("userid")==null) {
			return "redirect:/login";
		}
		User manager = userService.getUser(session.getAttribute("userid").toString());
		if(manager==null) {
			return "redirect:/login";
		}
		if(!manager.getRole().equals("PM")) {
			return "redirect:/login";
		}
		return "manage_project_user";
	}
	
	
	@PostMapping("/add-user-project")
	public String addUserToProject(@RequestParam("email") String userEmail, Model model, HttpSession session) {
		if(session.getAttribute("userid")==null) {
			return "redirect:/login";
		}
		User manager = userService.getUser(session.getAttribute("userid").toString());
		if(manager==null) {
			return "redirect:/login";
		}
		if(!manager.getRole().equals("PM")) {
			return "redirect:/login";
		}
		User user = userService.getUserByEmail(userEmail);
		Project project = projectService.getProjectOfManager(session.getAttribute("userid").toString());
		if(user!=null && project!=null) {
			String userId = user.getPhone();
			String projectId = project.getId();
			projectService.addUserToProject(userId, projectId);
		}
		return "manage_project_user";
		
	}
	
	
	@PostMapping("/delete-project")
	public String deleteProject(@RequestParam("projectId") String projectId, @RequestParam("confirmText") String confirmText) {
		if(confirmText.equals("delete")) {
			projectService.deleteProject(projectId);
		}
		return "redirect:/my-projects";
	}
	

}
