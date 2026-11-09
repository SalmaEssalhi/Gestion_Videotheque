<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/_header.jsp"><jsp:param name="titre" value="Emprunts"/></jsp:include>

<h2 class="mb-3">Tous les emprunts</h2>
<c:choose>
  <c:when test="${empty emprunts}">
    <div class="alert alert-info">Aucun emprunt.</div>
  </c:when>
  <c:otherwise>
    <table class="table table-striped bg-white">
      <thead><tr><th>#</th><th>Adhérent</th><th>Film</th><th>Emprunté le</th><th>Expire le</th><th>Statut</th></tr></thead>
      <tbody>
      <c:forEach var="e" items="${emprunts}">
        <tr>
          <td>${e.id}</td>
          <td><c:out value="${e.utilisateur.login}"/></td>
          <td><c:out value="${e.film.titre}"/></td>
          <td>${e.dateEmprunt}</td>
          <td>${e.dateExpiration}</td>
          <td><span class="badge bg-secondary">${e.statut}</span></td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/_footer.jsp"/>