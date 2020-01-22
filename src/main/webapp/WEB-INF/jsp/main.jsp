<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<fmt:setBundle basename="locale" />

<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <script src="${pageContext.request.contextPath}/js/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
    <script src="${pageContext.request.contextPath}/js/jquery.maskedinput.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/aes.js"></script>
    <title>Мои файлы</title>
</head>
<body >

<div class="content-box" id="content-box">
    <div class="content-buttons" id="content-buttons">
        <div id="content-button-add-company" class="content-button-add-company">
            <li>Добавить компанию</li>
        </div>
        <div id="content-button-edit-company" class="content-button-edit-company">
            <li>Редактировать компанию</li>
        </div>
        <div id="content-button-delete-company" class="content-button-delete-company">
            <li>Удалить компанию</li>
        </div>
    </div>
</div>

</body>
