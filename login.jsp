
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="imagetoolber" content="no" />
<meta name="description" content="" />
<meta name="keywords" content="" />
<link rel="stylesheet" href="./css/login.css">
<link rel="stylesheet" type=text/css href="./css/header.css">
<link rel="stylesheet" type=text/css href="./css/jupiter.css">
<script type="text/javascript" src="./js/header.js"></script>

<title>ログイン</title>
</head>
<body>
	<script type="text/javascript" src="./js/login.js"></script>
	<jsp:include page="header.jsp" />
	<div class="contents">
		<h1>ログイン画面</h1>
	</div>

	<s:if
		test="userIdErrorMessageList != null && userIdErrorMessageList.size > 0">
		<div class="error-message">
			<s:iterator value="userIdErrorMessageList">
				<s:property />
				<br>
			</s:iterator>
		</div>
	</s:if>

	<s:if
		test="passwordErrorMessageList != null && passwordErrorMessageList.size > 0">
		<div class="error-message">
			<s:iterator value="passwordErrorMessageList">
				<s:property />
				<br>
			</s:iterator>
		</div>
	</s:if>

	<s:if
		test="isNotUserInfoMessage != null && !isNotUserInfoMessage.isEmpty()">
		<div class="error-message">
			<s:property value="isNotUserInfoMessage" />
		</div>
	</s:if>

	<s:form id="loginForm">
		<table class="user-list">
			<tr>
				<th><s:label value="ユーザーID" /></th>
				<s:if test="#session.savedUserIdFlag == true">
					<td><s:textfield name="userId" class="txt"
							placeholder="ユーザーID" value='%{#session.savedUserId}'
							autocomplete="off" /></td>
				</s:if>
				<s:else>
					<td><s:textfield name="userId" class="txt"
							placeholder="ユーザーID" value="%{userId}" autocomplete="off" /></td>
				</s:else>
			</tr>
			<tr>
				<th><s:label value="パスワード" /></th>
				<td><s:password name="password" placeholder="パスワード" class="txt"
						autocomplete="off" /></td>
			</tr>
		</table>

		<s:if
			test="#session.savedUserIdFlag==true && #session.savedUserId !=null && !#session.savedUserId.isEmpty()">
			<s:checkbox name="savedUserIdFlag" checked="checked" class="checkbox" />
		</s:if>
		<s:else>
			<s:checkbox name="savedUserIdFlag" class="checkbox" />
		</s:else>
		<s:label value="ユーザーID保存" class="checkbox" />
		<div class="submit-btn-box">
			<s:submit value="ログイン" class="btn" onclick="goToLoginAction()" />
		</div>
		<div class="submit-btn-box">
			<s:submit value="新規ユーザー登録" class="btn" onclick="goCreateUserAction()" />
		</div>
		<div class="submit-btn-box">
			<s:submit value="パスワード再設定" class="btn"
				onclick="goResetPasswordAction()" />
		</div>
	</s:form>

</body>
</html>