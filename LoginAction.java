package com.internousdev.jupiter.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.jupiter.dao.CartInfoDAO;
import com.internousdev.jupiter.dao.UserInfoDAO;
import com.internousdev.jupiter.dto.CartInfoDTO;
import com.internousdev.jupiter.dto.UserInfoDTO;
import com.internousdev.jupiter.util.InputChecker;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport implements SessionAware {
	private Map<String, Object> session;
	private String userId;
	private String password;
	private boolean savedUserIdFlag;
	private List<String> userIdErrorMessageList;
	private List<String> passwordErrorMessageList;
	private String isNotUserInfoMessage;
	private List<String> CartInfoDTO;
	private int deleteProductedId;
	private int productId;
	private List<String> cartInfoList;
	private ArrayList<CartInfoDTO> cartInfoDTOListBySession = new ArrayList<CartInfoDTO>();
	private ArrayList<CartInfoDTO> cartInfoDTOList = new ArrayList<CartInfoDTO>();
	private CartInfoDAO cartInfoDAO = new CartInfoDAO();
	private int totalPrice = 0;

	public String execute() throws SQLException {
		// セッションのタイムアウトを記載
		if (session.isEmpty()) {
			return "sessionTimeout";
		}
		UserInfoDAO userInfoDAO = new UserInfoDAO();

		// ID保存チェック
		if (savedUserIdFlag) {
			session.put("savedUserIdFlag", true);
			session.put("savedUserId", userId);
		} else {
			session.remove("savedUserIdFlag");
			session.remove("savedUserId");
		}

		// (ⅱ)ID パスワードの認証チェック
		InputChecker inputChecker = new InputChecker();
		userIdErrorMessageList = inputChecker.doCheck("ユーザーID", userId, 1, 8, true, false, false, true, false, false,
				false);
		passwordErrorMessageList = inputChecker.doCheck("パスワード", password, 1, 16, true, false, false, true, false,
				false, false);
		if (userIdErrorMessageList.size() > 0 || passwordErrorMessageList.size() > 0) {

			return ERROR;
		}

		// （ⅰ）ログイン成功の場合
		String result = SUCCESS;
		if (!userInfoDAO.isExistsUserInfo(userId, password)) {

			isNotUserInfoMessage = "ユーザーIDまたはパスワードが異なります。";

			// (ⅲ)認証失敗してエラーメッセージ表示

			return ERROR;

		}

		// (ⅳ)カート画面への遷移
		if (userInfoDAO.isExistsUserInfo(userId, password)) {
			cartInfoDTOListBySession = cartInfoDAO.getCartInfoList(session.get("tempUserId").toString());
			if (cartInfoDTOListBySession != null) {
				changeCartInfo(cartInfoDTOListBySession);
			}
			if (session.containsKey("cartFlag")) {
				session.remove("cartFlag");
				result = "cart";
			} else {
				result = SUCCESS;
			}

			// ユーザー情報を登録
			userInfoDAO.login(userId, password);
			UserInfoDTO userInfoDTOEntry = userInfoDAO.getUserInfo(userId, password);
			session.put("userId", userInfoDTOEntry.getUserId());
			session.put("logined", userInfoDTOEntry.getLogined());
		}

		CartInfoDAO DAO = new CartInfoDAO();
		cartInfoDTOList = DAO.getCartInfoList(userId);

		for (CartInfoDTO i : cartInfoDTOList) {
			totalPrice += i.getSubtotal();
		}

		return result;

	}

	public ArrayList<CartInfoDTO> getCartInfoDTOListBySession() {
		return cartInfoDTOListBySession;
	}

	public void setCartInfoDTOListBySession(ArrayList<CartInfoDTO> cartInfoDTOListBySession) {
		this.cartInfoDTOListBySession = cartInfoDTOListBySession;
	}

	private void changeCartInfo(List<CartInfoDTO> cartInfoDTOListBySession) throws SQLException {
		CartInfoDAO cartInfoDAO = new CartInfoDAO();
		int count = 0;
		String tempUserId = session.get("tempUserId").toString();
		for (CartInfoDTO dto : cartInfoDTOListBySession) {
			if (cartInfoDAO.isExistsInCartItem(userId, dto.getProductId())) {
				count += cartInfoDAO.updateProductCount(userId, dto.getProductId(), dto.getProductCount());
				cartInfoDAO.deleteCartItem(dto.getId(), tempUserId);
			} else {
				count += cartInfoDAO.linkToUserId(userId, tempUserId, dto.getProductId());
			}
		}
		if (count == cartInfoDTOListBySession.size()) {
			cartInfoDAO.getCartInfoList(userId);

		}
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public ArrayList<CartInfoDTO> getCartInfoDTOList() {
		return cartInfoDTOList;
	}

	public void setCartInfoDTOList(ArrayList<CartInfoDTO> cartInfoDTOList) {
		this.cartInfoDTOList = cartInfoDTOList;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isSavedUserIdFlag() {
		return savedUserIdFlag;
	}

	public void setSavedUserIdFlag(boolean savedUserIdFlag) {
		this.savedUserIdFlag = savedUserIdFlag;
	}

	public List<String> getUserIdErrorMessageList() {
		return userIdErrorMessageList;
	}

	public void setUserIdErrorMessageList(List<String> userIdErrorMessageList) {
		this.userIdErrorMessageList = userIdErrorMessageList;
	}

	public List<String> getPasswordErrorMessageList() {
		return passwordErrorMessageList;
	}

	public void setPasswordErrorMessageList(List<String> passwordErrorMessageList) {
		this.passwordErrorMessageList = passwordErrorMessageList;
	}

	public String getIsNotUserInfoMessage() {
		return isNotUserInfoMessage;
	}

	public void setIsNotUserInfoMessage(String isNotUserInfoMessage) {
		this.isNotUserInfoMessage = isNotUserInfoMessage;
	}

	public List<String> getCartInfoDTO() {
		return CartInfoDTO;
	}

	public void setCartInfoDTO(List<String> cartInfoDTO) {
		CartInfoDTO = cartInfoDTO;
	}

	public int getDeleteProductedId() {
		return deleteProductedId;
	}

	public void setDeleteProductedId(int deleteProductedId) {
		this.deleteProductedId = deleteProductedId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public List<String> getCartInfoList() {
		return cartInfoList;
	}

	public void setCartInfoList(List<String> cartInfoList) {
		this.cartInfoList = cartInfoList;
	}

}
