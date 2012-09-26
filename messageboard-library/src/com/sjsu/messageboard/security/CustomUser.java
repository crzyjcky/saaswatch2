package com.sjsu.messageboard.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUser extends User {
	
	private String tenantId;
	
	public CustomUser(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked, Collection<GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, authorities);
	}

	public CustomUser(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked, Collection<GrantedAuthority> authorities,
			String tenantId) {
		
		this(username, password, enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, authorities);
		
		this.tenantId = tenantId;
	}
	
	public String getTenantId() {
		return tenantId;
	}

}
