<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="entete.jsp"/>
<h2>Les erreurs suivantes se sont produites</h2>
<ul>
    <c:forEach var="erreur" items="${erreurs}">
        <li><c:out value="${erreur}"/></li>
    </c:forEach>
</ul>
</body
