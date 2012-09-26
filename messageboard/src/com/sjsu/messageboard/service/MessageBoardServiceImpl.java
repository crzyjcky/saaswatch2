package com.sjsu.messageboard.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sjsu.messageboard.domain.Message;
import com.sjsu.messageboard.security.CustomUser;

public class MessageBoardServiceImpl implements MessageBoardService {
	
	private DataSource dataSource;
	private SimpleJdbcTemplate sjt;
	
	//private Map<Long, Message> messages = new LinkedHashMap<Long, Message>();

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		
		sjt = new SimpleJdbcTemplate(dataSource);
	}
	
	@Override
	public List<Message> listMessages() {
		//return new ArrayList<Message>(messages.values());
		CustomUser customUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		
		String sql = "SELECT id, author, title, body, tenant_id FROM messages WHERE tenant_id = :tenantId ORDER BY id";
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("tenantId", customUser.getTenantId());
		
		List<Map<String, Object>> rows = sjt.queryForList(sql, parameterSource);
		
		List<Message> messages = new ArrayList<Message>();
		
		int len = rows.size();
		for (int i = 0; i < len; i++) {
			Map<String, Object> row = rows.get(i);
			
			Message message = new Message();
			message.setId((Long) row.get("id"));
			message.setAuthor((String) row.get("author")); 
			message.setTitle((String) row.get("title"));
			message.setBody((String) row.get("body"));
			message.setTenantId((String) row.get("tenant_id"));
			
			messages.add(message);
		}
		
		return messages;
	}

	@Override
	public synchronized void postMessage(Message message) {
		CustomUser customUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//message.setId(System.currentTimeMillis());
		message.setTenantId(customUser.getTenantId());
		
		String sql = "INSERT INTO messages(author, title, body, tenant_id) VALUES(:author, :title, :body, :tenantId)";
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("author", message.getAuthor());
		parameterSource.addValue("title", message.getTitle());
		parameterSource.addValue("body", message.getBody());
		parameterSource.addValue("tenantId", message.getTenantId());
		sjt.update(sql, parameterSource);
	}

	@Override
	public synchronized void deleteMessage(Message message) {
		//messages.remove(message.getId());
		String sql = "DELETE FROM messages WHERE id = :id";
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("id", message.getId());
		sjt.update(sql, parameterSource);
	}

	@Override
	public Message findMessageById(Long messageId) {
		//return messages.get(messageId);
		
		String sql = "SELECT id, author, title, body, tenant_id FROM messages WHERE id = :id";
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("id", messageId);
		
		List<Map<String, Object>> rows = sjt.queryForList(sql, parameterSource);
		
		//List<Message> messages = new ArrayList<Message>();
		
		Message message = new Message();
		int len = rows.size();
		for (int i = 0; i < len; i++) {
			Map<String, Object> row = rows.get(i);
			
			//Message message = new Message();
			message.setId((Long) row.get("id"));
			message.setAuthor((String) row.get("author")); 
			message.setTitle((String) row.get("title"));
			message.setBody((String) row.get("body"));
			message.setTenantId((String) row.get("tenant_id"));
			
			//messages.add(message);
		}
		
		return message;
	}

}
