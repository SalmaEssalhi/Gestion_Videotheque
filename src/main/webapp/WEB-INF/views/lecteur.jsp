<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/_header.jsp"><jsp:param name="titre" value="Lecteur"/></jsp:include>

<c:choose>
  <c:when test="${not empty erreur}">
    <div class="alert alert-danger"><c:out value="${erreur}"/></div>
    <a class="btn btn-primary" href="${ctx}/catalogue">Retour au catalogue</a>
  </c:when>
  <c:otherwise>
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h2 class="mb-0"><c:out value="${emprunt.film.titre}"/></h2>
      <span class="badge bg-success">Accès jusqu'au <c:out value="${expiration}"/></span>
    </div>

    <div class="ratio ratio-16x9 bg-dark mb-3">
      <video controls controlsList="nodownload" preload="metadata">
        <source src="${ctx}/lecteur/flux?id=${emprunt.id}" type="video/mp4">
        Votre navigateur ne supporte pas la lecture vidéo.
      </video>
    </div>

    <p class="text-muted"><c:out value="${emprunt.film.synopsis}"/></p>
    <a class="btn btn-outline-secondary" href="${ctx}/mes-emprunts">Mes emprunts</a>
  </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/_footer.jsp"/>