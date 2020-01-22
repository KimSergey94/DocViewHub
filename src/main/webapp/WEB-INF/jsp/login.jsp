<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<fmt:setBundle basename="locale" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <script src="${pageContext.request.contextPath}/js/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
    <script src="${pageContext.request.contextPath}/js/jquery.maskedinput.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/aes.js"></script>
    <title>Авторизация</title>
</head>
<body >

<div class="content-box" id="content-box">
    <p class="error-message">
        <c:choose>
            <c:when test="${requestScope.error == 1000}">
                <fmt:message key = "key.incorrectLogin" />
            </c:when>
            <c:when test="${requestScope.error == 1001}">
                <fmt:message key = "key.incorrectPassword" />
            </c:when>
        </c:choose>
    </p>

    <div class="login-form" id="login-form">
        <form class="log-form" method="POST" action="${pageContext.request.contextPath}/login">
            <div id="user-login" class="user-login">
                <li>Введите логин: </li>
                <input type="text" class="login" id="login" name="login" required>
            </div>
            <div id="user-password" class="user-password">
                <li>Введите пароль: </li>
                <input type="password" class="password" id="password" name="password" required>
            </div>
            <input type="submit" class="sign-in" id="sign-in" value="Войти">
        </form>
    </div>
</div>

</body>
</html>