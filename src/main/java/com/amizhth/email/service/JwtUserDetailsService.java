package com.amizhth.email.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.amizhth.common.UtilConstants;
import com.amizhth.email.dao.UserAuthDao;
import com.amizhth.email.entitymodel.AuthModel;
import com.amizhth.email.util.emailUser;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	UserAuthDao userAuthDao;

	@Override
	public UserDetails loadUserByUsername(String username){
		AuthModel authModel = new AuthModel();
		List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
		int tenantID = 0;
		try {
			Object object = userAuthDao.findByMobilenumberEqualsAndStatusEquals(username, 1);

			if (object instanceof AuthModel) {
				authModel = (AuthModel) object;
				tenantID = authModel.getTenantid();

				
				return new emailUser(authModel.getMobilenumber(), authModel.getPassword(), true, true, true, true,
						authModel.getFirstname(), tenantID, authModel.getId(), grantedAuthorities);
			} else {
				throw new UsernameNotFoundException("User not Found :: "+ username);
			}
		} catch (LockedException  ex) {
			logger.error("Account Locked");
			throw new LockedException(ex.getMessage());
		}catch (Exception ex) {
			logger.error("Exception in loadUserByUsername :: "+ex.getMessage());
			return new emailUser(authModel.getMobilenumber(), authModel.getPassword(),
					false, false, false, false, username, tenantID, tenantID, grantedAuthorities);
		}

	}

	public UserDetails loadUserByUsernameAndTenantid(String username, int tenantid) throws UsernameNotFoundException {
		AuthModel authModel = new AuthModel();
		List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
		try {
			logger.info("Entered userName and tenant id");
			Object object = userAuthDao.findByMobilenumberEqualsAndStatusEqualsAndTenantid(username, 1, tenantid);
			logger.info("object :: " + object);
			if (object instanceof AuthModel) {
				authModel = (AuthModel) object;

				
				return new emailUser(authModel.getMobilenumber(), authModel.getPassword(), true, true, true, true,
						authModel.getFirstname(), tenantid, authModel.getId(), grantedAuthorities);
			} else {
				throw new UsernameNotFoundException("User Not Found :: " + username);
			}
		} catch (LockedException  ex) {
			logger.error("Account Locked");
			throw new LockedException(ex.getMessage());
		}catch (Exception ex) {
			logger.error("Exception in loadUserByUsername :: "+ex.getMessage());
			return new emailUser(authModel.getMobilenumber(), authModel.getPassword(),
					false, false, false, false, username, tenantid, tenantid, grantedAuthorities);
		}
	}

}