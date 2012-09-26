package com.sjsu.messageboard.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {

		String sql = "SELECT username, password, enabled, tenant_id FROM users where username = :username";
		
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("username", username);
		
		SimpleJdbcTemplate sjt = new SimpleJdbcTemplate(dataSource);
		CustomUser customUser = sjt.queryForObject(sql, new CustomUserMapper(), parameterSource);
		
		return customUser;
	}
	
	private List<GrantedAuthority> getGrantedAuthorities(String username) {
		
		String sql = "SELECT authority FROM authorities WHERE username = :username";
		
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("username", username);
		
		SimpleJdbcTemplate sjt = new SimpleJdbcTemplate(dataSource);
		List<Map<String, Object>> rows = sjt.queryForList(sql, parameterSource);
		
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		
		for (Map<String, Object> row : rows) {
			grantedAuthorities.add(new GrantedAuthorityImpl((String) row.get("authority")));
		}
		
		return grantedAuthorities;
	}
	
	private class CustomUserMapper implements ParameterizedRowMapper<CustomUser> {

		@Override
		public CustomUser mapRow(ResultSet rs, int arg1)
				throws SQLException {
			
			CustomUser customUser = new CustomUser(rs.getString("username"), rs.getString("password"), true, true, true, true, getGrantedAuthorities(rs.getString("username")), rs.getString("tenant_id"));
			return customUser;
		}
		
	}

}
