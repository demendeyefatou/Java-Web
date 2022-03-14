<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="entete.jsp"/>
<h2>Contenu de votre panier</h2>
<table border="1">
    <tr>
        <td>Article</td><td>Qte</td><td>Pu</td><td>Total</td>
    </tr>
    <c:forEach var="achat" items="${panier.achats}">
        <tr>
            <td><c:out value="${achat.article.nom}"/></td>
            <td><c:out value="${achat.qte}"/></td>
            <td><c:out value="${achat.article.prix}"/></td>
            <td><c:out value="${achat.total}"/></td>
            <td><a href="<c:out value="?action=retirerachat&id=${achat.article.id}"/>">Retirer</a></td>
        </tr>
    </c:forEach>
</table>
<p>
    Total de la commande : <c:out value="${panier.total}"/> euros
</p>
    </body>
    </html>
