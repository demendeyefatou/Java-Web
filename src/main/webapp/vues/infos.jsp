<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="entete.jsp"/>
<h2>Article d'id [<c:out value="${article.id}"/>]</h2>
<table border="1">
    <tr>
        <th>NOM</th><th>PRIX</th><th>STOCK ACTUEL</th><th>STOCK MINIMUM</th>
    </tr>
    <tr>
        <td><c:out value="${article.nom}"/></td>
        <td><c:out value="${article.prix}"/></td>
        <td><c:out value="${article.stockActuel}"/></td>
        <td><c:out value="${article.stockMinimum}"/></td>
    </tr>
</table>
<p>
<form method="post" action="?action=achat&id=<c:out value="${article.id}"/>"/>
<table>
    <tr>
        <td><input type="submit" value="Acheter"></td>
        <td>Qte <input type="text" name="qte" size="3" value="<c:out value="${qte}"/>"></td>
        <td><c:out value="${msg}"/></td>
    </tr>
</table>
</form>
</body>
</html>
