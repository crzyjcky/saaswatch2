package com.sjsu.messageboard.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sjsu.messageboard.domain.Message;
import com.sjsu.messageboard.security.CustomUser;
import com.sjsu.messageboard.service.MessageBoardService;

@Controller
@RequestMapping("/messageList*")
public class MessageListController {
	private MessageBoardService messageBoardService;
	
	@Autowired
	public MessageListController(MessageBoardService messageBoardService) {
		this.messageBoardService = messageBoardService;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String generateList(Model model) {		

		List<Message> messages = java.util.Collections.emptyList();
		messages = messageBoardService.listMessages();
		model.addAttribute("messages", messages);

		return "messageList";
	}
}