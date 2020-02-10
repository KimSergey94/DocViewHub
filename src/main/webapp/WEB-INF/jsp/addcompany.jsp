<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<fmt:setBundle basename="locale" />

<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <title>Мои файлы</title>
</head>
<body >
<div class="main-container">

    <form class="add-company-form" method="POST" action="${pageContext.request.contextPath}/addcompany">
        <div class="company-data-box" id="company-data-box">

            <div class="company-data" id="company-data-name-ru">
                <div id="company-name-ru" class="company-name-ru">
                    <li>Имя компании на русском: </li>
                    <input type="text" id="name_ru" name="name_ru">
                </div>
            </div> <br>
            <div class="company-data" id="company-data-name-kz">
                <div id="company-name-kz" class="company-name-kz">
                    <li>Имя компании на казахском: </li>
                    <input type="text" id="name-kz" name="name_kz">
                </div>
            </div> <br>
            <div class="company-data" id="company-data-bin">
                <div id="company-bin" class="company-bin">
                    <li>БИН: </li>
                    <input type="text" id="bin" name="bin" >
                </div>
            </div> <br>
            <div class="company-data" id="company-data-gov-org-num">
                <div id="company-gov-org-num" class="company-gov-org-num">
                    <li>Номер гос организации: </li>
                    <input type="text" id="gov-org-num" name="gov_org_num">
                </div>
            </div> <br>
            <div class="company-data" id="company-data-server-address">
                <div id="company-server-address" class="company-server-address">
                    <li>Адрес сервера: </li>
                    <input type="text" id="server-address" name="server_address">
                </div>
            </div> <br>
            <input class="send-button" type="submit" value="<fmt:message key = "key.addCompany"/>">
        </div>
    </form>



</div>


</body>
</html>