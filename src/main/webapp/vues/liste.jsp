<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="entete.jsp"/>
<h2>Liste des articles</h2>
<table border="1">
    <tr>
        <th>NOM</th><th>PRIX</th>
    </tr>
    <c:forEach var="article" items="${listarticles}">
        <tr>
            <td><c:out value="${article.nom}"/></td>
            <td><c:out value="${article.prix}"/></td>
            <td><a href="<c:out value="?action=infos&id=${article.id}"/>">Infos</a></td>
        </tr>
    </c:forEach>
</table>
<p>
    <c:out value="${message}"/>
</p>

    </body>
    </html>
