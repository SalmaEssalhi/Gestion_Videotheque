<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/_header.jsp"><jsp:param name="titre" value="Film"/></jsp:include>

<h2 class="mb-3">${empty film.id ? 'Nouveau film' : 'Modifier le film'}</h2>
<c:if test="${not empty erreur}">
  <div class="alert alert-danger"><c:out value="${erreur}"/></div>
</c:if>

<form method="post" action="${ctx}/admin/films" class="row g-3 bg-white p-3 border rounded">
  <input type="hidden" name="id" value="${film.id}">

  <div class="col-md-6">
    <label class="form-label">Titre *</label>
    <input type="text" class="form-control" name="titre" required value="<c:out value='${film.titre}'/>">
  </div>
  <div class="col-md-6">
    <label class="form-label">Réalisateur</label>
    <input type="text" class="form-control" name="realisateur" value="<c:out value='${film.realisateur}'/>">
  </div>
  <div class="col-md-3">
    <label class="form-label">Année de sortie</label>
    <input type="number" class="form-control" name="anneeSortie"
           value="${film.anneeSortie == 0 ? '' : film.anneeSortie}">
  </div>
  <div class="col-md-3">
    <label class="form-label">Durée (minutes)</label>
    <input type="number" class="form-control" name="dureeMinutes"
           value="${film.dureeMinutes == 0 ? '' : film.dureeMinutes}">
  </div>
  <div class="col-md-3">
    <label class="form-label">Prix de location (MAD) *</label>
    <input type="text" class="form-control" name="prixLocation" required value="${film.prixLocation}">
  </div>
  <div class="col-md-3">
    <label class="form-label">Catégorie *</label>
    <select class="form-select" name="categorieId" required>
      <option value="">-- Choisir --</option>
      <c:forEach var="cat" items="${categories}">
        <option value="${cat.id}" ${film.categorie.id == cat.id ? 'selected' : ''}>
          <c:out value="${cat.nom}"/>
        </option>
      </c:forEach>
    </select>
  </div>
  <div class="col-12">
    <label class="form-label">Synopsis</label>
    <textarea class="form-control" name="synopsis" rows="4"><c:out value="${film.synopsis}"/></textarea>
  </div>
  <div class="col-md-6">
    <label class="form-label">URL de l'affiche</label>
    <input type="text" class="form-control" name="afficheUrl" value="<c:out value='${film.afficheUrl}'/>">
  </div>
  <div class="col-md-6">
    <label class="form-label">Lien ou chemin de la vidéo</label>
    <input type="text" class="form-control" name="urlVideo" value="<c:out value='${film.urlVideo}'/>">
  </div>
  <div class="col-12 form-check ms-2">
    <input type="checkbox" class="form-check-input" id="publie" name="publie"
           <c:if test="${film.publie}">checked</c:if>>
    <label class="form-check-label" for="publie">Publié (visible dans le catalogue)</label>
  </div>
  <div class="col-12">
    <button class="btn btn-primary">Enregistrer</button>
    <a class="btn btn-link" href="${ctx}/admin/films">Annuler</a>
  </div>
</form>
<jsp:include page="/WEB-INF/views/_footer.jsp"/>