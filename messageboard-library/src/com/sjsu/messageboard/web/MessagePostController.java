package com.sjsu.messageboard.web;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sjsu.messageboard.domain.Message;
import com.sjsu.messageboard.security.CustomUser;
import com.sjsu.messageboard.service.MessageBoardService;

@Controller
@RequestMapping("/messagePost*")
public class MessagePostController {

	private MessageBoardService messageBoardService;
	
	@Autowired
	public void MessagePostController(MessageBoardService messageBoardService) {
		this.messageBoardService = messageBoardService;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String setupForm(Model model) {
		Message message = new Message();
		model.addAttribute("message", message);
		return "messagePost";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(@ModelAttribute("message") Message message,
			BindingResult result) {
		if (result.hasErrors()) {
			return "messagePost";
		} else {
			
			CustomUser customUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			message.setAuthor(customUser.getUsername());
			messageBoardService.postMessage(message);
			
			return "redirect:messageList";
		}
	}
}
