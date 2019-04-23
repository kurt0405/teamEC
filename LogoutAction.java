package com.internousdev.jupiter.action;


import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.jupiter.dao.UserInfoDAO;
import com.opensymphony.xwork2.ActionSupport;

	public class LogoutAction extends ActionSupport implements SessionAware{
		private Map<String, Object> session;

		public String execute() {
			UserInfoDAO userInfoDAO = new UserInfoDAO();
			String userId = String.valueOf(session.get("userId"));
			boolean savedUserId = Boolean.valueOf(String.valueOf(session.get("savedUserIdFlag")));
			int count = userInfoDAO.logout(userId);
			if(count > 0) {
				session.clear();
				if(savedUserId){
					session.put("savedUserIdFlag", savedUserId);
					session.put("savedUserId", userId);
				}
			}
			return SUCCESS;
		}

		public Map<String, Object> getSession() {
			return session;
		}
		public void setSession(Map<String, Object> session) {
			this.session = session;
		}

	}


