<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<jsp:include page="/WEB-INF/views/_header.jsp"><jsp:param name="titre" value="${film.titre}"/></jsp:include>

<div class="row g-4">
  <div class="col-md-4">
    <c:choose>
      <c:when test="${not empty film.afficheUrl}">
        <c:choose>
          <c:when test="${fn:startsWith(film.afficheUrl, 'http')}">
            <img class="img-fluid rounded shadow-sm" src="<c:out value='${film.afficheUrl}'/>" alt="Affiche">
          </c:when>
          <c:otherwise>
            <img class="img-fluid rounded shadow-sm" src="${ctx}/images/affiches/<c:out value='${film.afficheUrl}'/>" alt="Affiche">
          </c:otherwise>
        </c:choose>
      </c:when>
      <c:otherwise>
        <div class="bg-secondary text-white d-flex align-items-center justify-content-center rounded" style="height:400px;">Pas d'affiche</div>
      </c:otherwise>
    </c:choose>
  </div>

  <div class="col-md-8">
    <h2><c:out value="${film.titre}"/></h2>
    <p class="text-muted">
      <c:out value="${film.categorie.nom}"/>
      <c:if test="${film.anneeSortie > 0}"> · ${film.anneeSortie}</c:if>
      <c:if test="${film.dureeMinutes > 0}"> · ${film.dureeMinutes} min</c:if>
    </p>

    <c:if test="${not empty film.realisateur}">
      <p><strong>Réalisateur :</strong> <c:out value="${film.realisateur}"/></p>
    </c:if>

    <p><c:out value="${film.synopsis}"/></p>

    <p class="fs-4">
      <strong><fmt:formatNumber value="${film.prixLocation}" minFractionDigits="2"/> MAD</strong>
      <span class="text-muted fs-6">· accès 48 h</span>
    </p>

    <c:choose>
      <c:when test="${empty sessionScope.user}">
        <a class="btn btn-primary" href="${ctx}/login">Se connecter pour emprunter</a>
      </c:when>
      <c:when test="${film.publie}">
        <form method="post" action="${ctx}/emprunt" class="d-inline">
          <input type="hidden" name="filmId" value="${film.id}">
          <button class="btn btn-primary">Emprunter</button>
        </form>
      </c:when>
      <c:otherwise>
        <span class="badge bg-secondary">Brouillon : non empruntable</span>
      </c:otherwise>
    </c:choose>
    <a class="btn btn-outline-secondary" href="${ctx}/catalogue">Retour au catalogue</a>
  </div>
</div>

<jsp:include page="/WEB-INF/views/_footer.jsp"/>