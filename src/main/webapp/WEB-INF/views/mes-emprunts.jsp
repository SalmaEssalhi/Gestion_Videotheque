<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<jsp:include page="/WEB-INF/views/_header.jsp"><jsp:param name="titre" value="Mes emprunts"/></jsp:include>

<h2 class="mb-4">Mes emprunts</h2>

<c:if test="${empty emprunts}">
  <div class="alert alert-info">Aucun emprunt pour le moment.
    <a href="${ctx}/catalogue">Parcourir le catalogue</a></div>
</c:if>

<c:if test="${not empty emprunts}">
<div class="table-responsive">
  <table class="table table-striped align-middle bg-white">
    <thead>
      <tr><th>Film</th><th>Statut</th><th>Expire le</th><th></th></tr>
    </thead>
    <tbody>
    <c:forEach var="e" items="${emprunts}">
      <tr>
        <td><a href="${ctx}/film?id=${e.film.id}"><c:out value="${e.film.titre}"/></a></td>
        <td>
          <span class="badge ${e.statut == 'EN_COURS' ? 'bg-success' : (e.statut == 'EN_ATTENTE_PAIEMENT' ? 'bg-warning text-dark' : 'bg-secondary')}">
            ${e.statut}
          </span>
        </td>
        <td>${fn:replace(fn:substring(e.dateExpiration, 0, 16), 'T', ' ')}</td>
        <td class="text-end text-nowrap">
          <c:if test="${e.statut == 'EN_COURS'}">
            <a class="btn btn-primary btn-sm" href="${ctx}/lecteur?idEmprunt=${e.id}">Visionner</a>
            <form method="post" action="${ctx}/emprunt" class="d-inline"
                  onsubmit="return confirm('Rendre ce film ? Aucun remboursement.');">
              <input type="hidden" name="action" value="rendre">
              <input type="hidden" name="id" value="${e.id}">
              <button class="btn btn-outline-secondary btn-sm">Rendre</button>
            </form>
          </c:if>
          <c:if test="${e.statut == 'EN_ATTENTE_PAIEMENT'}">
            <a class="btn btn-warning btn-sm" href="${ctx}/paiement?id=${e.id}">Payer</a>
          </c:if>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>
</c:if>

<jsp:include page="/WEB-INF/views/_footer.jsp"/>