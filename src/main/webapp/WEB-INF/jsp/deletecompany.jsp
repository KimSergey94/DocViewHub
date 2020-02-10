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
    <form class="delete-company-form" method="POST" action="${pageContext.request.contextPath}/deletecompany">
        <div class="company-data-box" id="company-data-box">
            <div class="select-company-data-to-delete" id="company-data-to-delete">
                <div id="company-to-delete-div" class="company-to-delete-div">
                    <select onchange="getCompanyInfo(this)" id="company-to-delete" class="data" >
                        <option disabled selected value="0"><fmt:message key = "key.companies"/></option>
                        <c:forEach var="company" items="${requestScope.companies}">
                            <option value="${company.id}">${company.nameRU}</option>
                        </c:forEach>
                    </select>
                </div>
            </div> <br>

            <div class="company-data-to-delete" id="company-data-to-delete-name-ru">
                <div id="company-to-delete-name-ru" class="company-to-delete-name-ru">
                    <li>Имя компании на русском: </li>
                    <input type="text" id="name-ru" name="name_ru" readonly>
                </div>
            </div> <br>
            <div class="company-data-to-delete" id="company-data-to-delete-name-kz">
                <div id="company-to-delete-name-kz" class="company-to-delete-name-kz">
                    <li>Имя компании на казахском: </li>
                    <input type="text" id="name-kz" name="name_kz" readonly>
                </div>
            </div> <br>
            <div class="company-data-to-delete" id="company-data-to-delete-bin">
                <div id="company-to-delete-bin" class="company-to-delete-bin">
                    <li>БИН: </li>
                    <input type="text" id="bin" name="bin" readonly>
                </div>
            </div> <br>
            <div class="company-data-to-delete" id="company-data-to-delete-gov-org-num">
                <div id="company-to-delete-gov-org-num" class="company-to-delete-gov-org-num">
                    <li>Номер гос организации: </li>
                    <input type="text" id="gov-org-num" name="gov_org_num" readonly>
                </div>
            </div> <br>
            <div class="company-data-to-delete" id="company-data-to-delete-server-address">
                <div id="company-to-delete-server-address" class="company-to-delete-server-address">
                    <li>Адрес сервера: </li>
                    <input type="text" id="server-address" name="server_address" readonly>
                </div>
            </div> <br>
            <input type="hidden" name="companyID" id="companyID">
            <input class="delete-button" id="delete-button" type="submit" value="<fmt:message key = "key.deleteCompany"/>">
        </div>
    </form>
</div>
</body>
</html>