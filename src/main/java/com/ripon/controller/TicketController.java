package com.ripon.controller;

import java.sql.Time;
import java.util.Date;
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
import org.springframework.web.context.annotation.SessionScope;

import com.ripon.entity.Project;
import com.ripon.entity.Ticket;
import com.ripon.entity.TicketResource;
import com.ripon.entity.User;
import com.ripon.service.ProjectService;
import com.ripon.service.TicketResourceService;
import com.ripon.service.TicketService;
import com.ripon.service.UserService;

@Controller
public class TicketController {
	
	@Autowired
	private TicketService ticketService;
	@Autowired
	private UserService userService;
	@Autowired
	private Ticket ticket;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private TicketResourceService ticketResourceService;
	
	@GetMapping("/create-ticket")
	public String getTicketPage(Model model, HttpSession session) {
		if(session.getAttribute("userid")==null) {
			return "redirect:/login";
		}
		
		User user = userService.getUser(session.getAttribute("userid").toString());
		if(user!=null && user.getRole().equals("PM")) {
			model.addAttribute("ticket", ticket);
			return "create_ticket";
		}
		
		return "redirect:/login";
		
	}
	
	
	
	@PostMapping("/create-ticket")
	public String createTicket(@Valid @ModelAttribute("ticket") Ticket ticket, BindingResult result, Model model,
			HttpSession session) {
		if(session.getAttribute("userid")==null) {
			return "redirect:/login";
		}
		User user = userService.getUser(session.getAttribute("userid").toString());
		if(user==null || !user.getRole().equals("PM")) {
			return "redirect:/login";
		}
		if(result.hasErrors()) {
			return "create_ticket";
		}
		//System.out.println(result.getErrorCount());
		
		// saving date and time
		Date date = new Date();
		java.sql.Date sqldate = new java.sql.Date(date.getTime());
		java.sql.Time sqltime = new Time(date.getTime());
		ticket.setCreatedDate(sqldate.toString());
		ticket.setCreatedTime(sqltime.toString());
		ticket.setLastUpdatedDate(sqldate.toString());
		ticket.setLastUpdatedTime(sqltime.toString());
		
		// saving status
		ticket.setStatus("New");
		
		// saving project_id and project_manager_id
		Project project = projectService.getProjectOfManager(session.getAttribute("userid").toString());
		// don't have any project
		if(project==null) {
			model.addAttribute("status_failure", "You are not assigned to any project !");
			return "create_ticket";
		}
		ticket.setProjectId(Long.parseLong(project.getId()));
		ticket.setManagerId(Long.parseLong(project.getManagerId()));
		boolean status = ticketService.saveTicket(ticket);
		
		if(status) {
			model.addAttribute("status_success", "Your ticket is created successfully !");
			return "create_ticket";
		}
		
		model.addAttribute("status_failure", "Oops, something went wrong !");
		return "create_ticket";
		
	}
	
	 
	@GetMapping("/ticket-detail/{ticketId}")
	public String getTicketDetail(@PathVariable("ticketId") String ticketId, HttpSession session, Model model) {
		if(session.getAttribute("userid")==null) {
			return "redirect:/login";
		}
		
		Ticket ticket = ticketService.getTicket(ticketId);
		// ticket info
		model.addAttribute("id", ticket.getId());
		model.addAttribute("title", ticket.getTitle());
		model.addAttribute("descr", ticket.getDescr());
		model.addAttribute("createdDate", ticket.getCreatedDate());
		model.addAttribute("createdTime", ticket.getCreatedTime());
		model.addAttribute("updatedDate", ticket.getLastUpdatedDate());
		model.addAttribute("updatedTime", ticket.getLastUpdatedTime());
		model.addAttribute("type", ticket.getType());
		model.addAttribute("status", ticket.getStatus());
		model.addAttribute("priority", ticket.getPriority());
		model.addAttribute("id", ticket.getId());
		
		// user info
		User user = userService.getUser(ticket.getAssignedUserId()+"");
		if(user != null) {
			model.addAttribute("isAssigned", true);
			model.addAttribute("userName", user.getFname() + " " + user.getLname());
			model.addAttribute("email", user.getEmail());
			model.addAttribute("phone", user.getPhone());
			model.addAttribute("role", User.ROLE.get(user.getRole()));
			model.addAttribute("image", "/profile-image/" + user.getImage());
		}else {
			model.addAttribute("isAssigned", false);
		}
		
		// get list of all users in the project
		List<User> allUsersList = projectService.getUsersOfProject(ticket.getProjectId()+"");
		model.addAttribute("allUsersList", allUsersList);
		
		// get all the resources of ticket
		List<TicketResource> resources = ticketResourceService.getTicketResources(ticketId);
		model.addAttribute("resources", resources);
		model.addAttribute("resourceCount", resources.size());
		return "ticket_detail";
	}
	
	
	
	@GetMapping("/show-tickets")
	public String getProjectManagerTickets(Model model, HttpSession session) {
		if(session.getAttribute("userid")==null) {
			return "redirect:/login";
		}
		User manager = userService.getUser(session.getAttribute("userid").toString());
		if(manager==null) {
			return "redirect:/login";
		}
		List<Ticket> ticketList = null;
		if(manager.getRole().equals("PM")) {
			ticketList = ticketService.getTicketsOfProjectManager(session.getAttribute("userid").toString());
		}
		if(manager.getRole().equals("DV") || manager.getRole().equals("TS")) {
			ticketList = ticketService.getTicketsOfUser(session.getAttribute("userid").toString());
		}
		model.addAttribute("ticketList", ticketList);
		model.addAttribute("ticketCount", ticketList.size());
		return "show_tickets";
	}

	// assign ticket to a user
	@GetMapping("/assign-ticket-to-user")
	public String assignTicketToUser(@RequestParam("userId") String userId, @RequestParam("ticketId") String ticketId) {
		ticketService.assignTicketToUser(userId, ticketId);
		return "redirect:/ticket-detail/"+ticketId;
	}
	
	// unassign user from a ticket
	@CrossOrigin
	@ResponseBody
	@GetMapping("/unassign-ticket-of-user")
	public Boolean unassignTicketOfUser(@RequestParam("ticketId") String ticketId) {
		return ticketService.unassignTicketOfUser(ticketId);
	}
	

}
