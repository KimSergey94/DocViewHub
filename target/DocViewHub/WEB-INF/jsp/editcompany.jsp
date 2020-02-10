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
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
    <title>Мои файлы</title>
</head>
<body >
<div class="main-container">
    <form class="edit-company-form" method="POST" enctype="multipart/form-data" action="${pageContext.request.contextPath}/editcompany">
        <div class="company-data-box" id="company-data-box">
            <div class="select-company-data-to-edit" id="company-data-to-edit">
                <div id="company-to-edit-div" class="company-to-edit-div">
                    <select onchange="getCompanyInfo(this)" id="company-to-edit" class="data">
                        <option disabled selected value="0"><fmt:message key = "key.companies"/></option>
                        <c:forEach var="company" items="${requestScope.companies}">
                            <option value="${company.id}">${company.nameRU}</option>
                        </c:forEach>
                    </select>
                </div>
            </div> <br>
            <div class="company-data-to-edit" id="company-data-to-edit-name-ru">
                <div id="company-name-ru" class="company-name-ru">
                    <li>Имя компании на русском: </li>
                    <input type="text" id="name-ru" name="name_ru">
                </div>
            </div> <br>
            <div class="company-data-to-edit" id="company-data-to-edit-name-kz">
                <div id="company-name-kz" class="company-name-kz">
                    <li>Имя компании на казахском: </li>
                    <input type="text" id="name-kz" name="name_kz">
                </div>
            </div> <br>
            <div class="company-data-to-edit" id="company-data-to-edit-bin">
                <div id="company-bin" class="company-bin">
                    <li>БИН: </li>
                    <input type="text" id="bin" name="bin" >
                </div>
            </div> <br>
            <div class="company-data-to-edit" id="company-data-to-edit-gov-org-num">
                <div id="company-gov-org-num" class="company-gov-org-num">
                    <li>Номер гос организации: </li>
                    <input type="text" id="gov-org-num" name="gov_org_num">
                </div>
            </div> <br>
            <div class="company-data-to-edit" id="company-data-to-edit-server-address">
                <div id="company-server-address" class="company-server-address">
                    <li>Адрес сервера: </li>
                    <input type="text" id="server-address" name="server_address">
                </div>
            </div> <br>
            <input type="hidden" name="companyID" id="companyID">
            <input class="edit-button" id="edit-button" type="submit" value="<fmt:message key = "key.editCompany"/>">
        </div>
    </form>
</div>
</body>
</html>