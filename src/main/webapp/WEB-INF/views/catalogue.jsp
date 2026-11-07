<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/_header.jsp"><jsp:param name="titre" value="Catalogue"/></jsp:include>

<h2 class="mb-4">Catalogue</h2>

<form class="card card-body mb-4" method="get" action="${ctx}/catalogue">
  <div class="row g-3 align-items-end">
    <div class="col-md-3">
      <label class="form-label">Titre</label>
      <input type="text" class="form-control" name="titre" value="<c:out value='${titre}'/>">
    </div>
    <div class="col-md-3">
      <label class="form-label">Réalisateur</label>
      <input type="text" class="form-control" name="realisateur" value="<c:out value='${realisateur}'/>">
    </div>
    <div class="col-md-3">
      <label class="form-label">Catégorie</label>
      <select class="form-select" name="cat">
        <option value="">Toutes</option>
        <c:forEach var="cat" items="${categories}">
          <option value="${cat.id}" ${cat.id == catId ? 'selected' : ''}><c:out value="${cat.nom}"/></option>
        </c:forEach>
      </select>
    </div>
    <div class="col-md-2">
      <label class="form-label">Année</label>
      <input type="number" class="form-control" name="annee" value="${annee}">
    </div>
    <div class="col-md-1">
      <button class="btn btn-primary w-100">OK</button>
    </div>
  </div>
</form>

<c:if test="${empty resultats.items}">
  <div class="alert alert-info">Aucun film trouvé.</div>
</c:if>

<div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-3 mb-4">
  <c:forEach var="f" items="${resultats.items}">
    <div class="col">
      <div class="card h-100 film-card">
        <c:choose>
          <c:when test="${not empty f.afficheUrl}">
            <img src="<c:out value='${f.afficheUrl}'/>" alt="Affiche">
          </c:when>
          <c:otherwise><div class="noimg">Pas d'affiche</div></c:otherwise>
        </c:choose>
        <div class="card-body">
          <h3 class="h6"><c:out value="${f.titre}"/></h3>
          <div class="text-muted small">
            <c:out value="${f.categorie.nom}"/> · ${f.anneeSortie} · ${f.dureeMinutes} min
          </div>
          <p class="small mt-2"><c:out value="${f.synopsis}"/></p>
        </div>
        <div class="card-footer d-flex justify-content-between">
          <strong><fmt:formatNumber value="${f.prixLocation}" minFractionDigits="2"/> MAD</strong>
          <span class="text-muted small">48 h</span>
        </div>
      </div>
    </div>
  </c:forEach>
</div>

<jsp:include page="/WEB-INF/views/_pagination.jsp"/>
<jsp:include page="/WEB-INF/views/_footer.jsp"/>