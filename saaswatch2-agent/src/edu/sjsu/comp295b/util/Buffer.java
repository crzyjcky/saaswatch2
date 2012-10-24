package edu.sjsu.comp295b.util;

import java.io.IOException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Buffer {

	private static final Logger logger = LoggerFactory.getLogger(Buffer.class);
	
	Connection conn;
			
	public Buffer() {
		
		try {
			//Server server = Server.c.createTcpServer().start();
			
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection("jdbc:h2:~/saaswatch2;AUTO_SERVER=TRUE", "sa", "");// "jdbc:h2:~/test", "sa", "");
		} catch (ClassNotFoundException e) {

			logger.error("constructor", e);
		} catch (SQLException e) {

			logger.error("constructor", e);
		} 
	}
	
	public void addData(String type, Object data) {
		String json = null;
		ObjectMapper mapper = new ObjectMapper();
		
		try {

			json = mapper.writeValueAsString(data);
		} catch (JsonGenerationException e) {
			
			logger.error("addData", e);
		} catch (JsonMappingException e) {
			
			logger.error("addData", e);
		} catch (IOException e) {
			
			logger.error("addData", e);
		}
		
		String sql = "INSERT INTO buffer(data_type, data) VALUES(?, ?)";
		try {
			Clob clob = conn.createClob();
			clob.setString(1, json);
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, type);
			ps.setClob(2, clob);
			ps.executeUpdate();
			
			clob.free();
		} catch (SQLException e) {

			logger.error("addData", e);
		}
	}
	
	public List<String[]> retrieveAndClear() {
		
		List<String[]> dataList = new ArrayList<String[]>();
		
		String sql = "SELECT data_type, data FROM buffer";
		String sql2 = "TRUNCATE TABLE buffer";
		
		
		try {
			conn.setAutoCommit(false);
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				
				String[] columns = new String[2];
				columns[0] = rs.getString(1);
				columns[1] = rs.getString(2);
				
				dataList.add(columns);
			}
			
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			ps2.executeUpdate();	
			
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {

			logger.error("retrieveAndClear", e);
		}
		
		return dataList;
	}
}
