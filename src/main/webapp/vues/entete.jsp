<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>webarticles</title>
</head>
<body>
<table>
    <tr>
        <td><h2>Magasin virtuel</h2></td>
        <c:forEach items="${actions}" var="action">
            <td>|</td>
            <td><a href="<c:out value="${action.href}"/>"><c:out value="${action.lien}"/></a></td>
        </c:forEach>
    </tr>
</table>
</body>
</html>