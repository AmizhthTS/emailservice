package com.amizhth.email.util;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class emailUser extends User {

	public emailUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked, String firstname, int tenantid, int userid,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

		this.firstname = firstname;
		this.tenantid = tenantid;
		this.userid = userid;
	}

	private static final long serialVersionUID = 1L;

	private String firstname;

	private Integer tenantid;

	private Integer userid;

	public String getFirstname() {
		return firstname;
	}

	public int getTenantid() {
		return tenantid;
	}

	public int getUserid() {
		return userid;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setTenantid(int tenantid) {
		this.tenantid = tenantid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

}
