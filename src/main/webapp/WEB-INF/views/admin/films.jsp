<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/_header.jsp"><jsp:param name="titre" value="Films"/></jsp:include>

<div class="d-flex justify-content-between align-items-center mb-3">
  <h2 class="mb-0">Films</h2>
  <a class="btn btn-success" href="${ctx}/admin/films?action=new">+ Nouveau film</a>
</div>

<form class="row g-2 mb-3" method="get" action="${ctx}/admin/films">
  <div class="col-md-6">
    <input type="text" name="titre" class="form-control" placeholder="Titre"
           value="<c:out value='${titre}'/>">
  </div>
  <div class="col-auto"><button class="btn btn-outline-secondary">Rechercher</button></div>
</form>

<table class="table table-striped bg-white align-middle">
  <thead>
    <tr><th>Titre</th><th>Catégorie</th><th>Année</th><th>Prix</th><th>Statut</th>
        <th class="text-end">Actions</th></tr>
  </thead>
  <tbody>
  <c:forEach var="f" items="${resultats.items}">
    <tr>
      <td><c:out value="${f.titre}"/></td>
      <td><c:out value="${f.categorie.nom}"/></td>
      <td>${f.anneeSortie}</td>
      <td><fmt:formatNumber value="${f.prixLocation}" minFractionDigits="2"/> MAD</td>
      <td><span class="badge ${f.publie ? 'bg-success' : 'bg-secondary'}">${f.publie ? 'Publié' : 'Brouillon'}</span></td>
      <td class="text-end">
        <a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/films?action=edit&id=${f.id}">Modifier</a>
        <form class="d-inline" method="post" action="${ctx}/admin/films">
          <input type="hidden" name="action" value="toggle">
          <input type="hidden" name="id" value="${f.id}">
          <button class="btn btn-sm btn-outline-secondary">${f.publie ? 'Dépublier' : 'Publier'}</button>
        </form>
        <form class="d-inline" method="post" action="${ctx}/admin/films"
              onsubmit="return confirm('Supprimer ce film ?');">
          <input type="hidden" name="action" value="delete">
          <input type="hidden" name="id" value="${f.id}">
          <button class="btn btn-sm btn-outline-danger">Supprimer</button>
        </form>
      </td>
    </tr>
  </c:forEach>
  </tbody>
</table>

<jsp:include page="/WEB-INF/views/_pagination.jsp"/>
<jsp:include page="/WEB-INF/views/_footer.jsp"/>