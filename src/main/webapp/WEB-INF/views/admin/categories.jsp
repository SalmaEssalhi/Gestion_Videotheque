<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/_header.jsp"><jsp:param name="titre" value="Catégories"/></jsp:include>

<h2 class="mb-3">Catégories</h2>
<div class="row">
  <div class="col-md-4">
    <div class="card card-body mb-3">
      <h6>${empty categorie ? 'Nouvelle catégorie' : 'Modifier la catégorie'}</h6>
      <form method="post" action="${ctx}/admin/categories">
        <input type="hidden" name="id" value="${categorie.id}">
        <input type="text" class="form-control mb-2" name="nom" placeholder="Nom" required
               value="<c:out value='${categorie.nom}'/>">
        <textarea class="form-control mb-2" name="description" rows="3"
                  placeholder="Description"><c:out value="${categorie.description}"/></textarea>
        <button class="btn btn-primary">Enregistrer</button>
        <c:if test="${not empty categorie}">
          <a class="btn btn-link" href="${ctx}/admin/categories">Annuler</a>
        </c:if>
      </form>
    </div>
  </div>
  <div class="col-md-8">
    <table class="table table-striped bg-white align-middle">
      <thead><tr><th>Nom</th><th>Description</th><th class="text-end">Actions</th></tr></thead>
      <tbody>
      <c:forEach var="cat" items="${categories}">
        <tr>
          <td><c:out value="${cat.nom}"/></td>
          <td><c:out value="${cat.description}"/></td>
          <td class="text-end">
            <a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/categories?edit=${cat.id}">Modifier</a>
            <form class="d-inline" method="post" action="${ctx}/admin/categories"
                  onsubmit="return confirm('Supprimer cette catégorie ?');">
              <input type="hidden" name="action" value="delete">
              <input type="hidden" name="id" value="${cat.id}">
              <button class="btn btn-sm btn-outline-danger">Supprimer</button>
            </form>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</div>
<jsp:include page="/WEB-INF/views/_footer.jsp"/>