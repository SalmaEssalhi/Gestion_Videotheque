<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/_header.jsp"><jsp:param name="titre" value="Mes paiements"/></jsp:include>

<h2 class="mb-3">Mes paiements</h2>
<c:choose>
  <c:when test="${empty paiements}">
    <div class="alert alert-info">Aucun paiement.</div>
  </c:when>
  <c:otherwise>
    <table class="table table-striped bg-white">
      <thead><tr><th>Date</th><th>Film</th><th>Montant</th><th>Carte</th><th>Référence</th><th>Statut</th></tr></thead>
      <tbody>
      <c:forEach var="p" items="${paiements}">
        <tr>
          <td>${p.datePaiement}</td>
          <td><c:out value="${p.emprunt.film.titre}"/></td>
          <td><fmt:formatNumber value="${p.montant}" minFractionDigits="2"/> ${p.devise}</td>
          <td>**** <c:out value="${p.carteMasquee}"/></td>
          <td><c:out value="${p.referenceTransaction}"/></td>
          <td><span class="badge ${p.statut == 'REUSSI' ? 'bg-success' : 'bg-danger'}">${p.statut}</span></td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/_footer.jsp"/>