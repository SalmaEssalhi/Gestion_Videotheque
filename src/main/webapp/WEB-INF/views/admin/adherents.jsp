<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/_header.jsp"><jsp:param name="titre" value="Adhérents"/></jsp:include>

<h2 class="mb-3">Adhérents</h2>
<div class="row">
  <div class="col-lg-4">
    <div class="card card-body mb-3">
      <h6>${empty adherent ? 'Nouvel adhérent' : 'Modifier l\'adhérent'}</h6>
      <form method="post" action="${ctx}/admin/adherents">
        <input type="hidden" name="id" value="${adherent.id}">
        <input type="text" class="form-control mb-2" name="nom" placeholder="Nom" required
               value="<c:out value='${adherent.nom}'/>">
        <input type="text" class="form-control mb-2" name="prenom" placeholder="Prénom" required
               value="<c:out value='${adherent.prenom}'/>">
        <input type="email" class="form-control mb-2" name="email" placeholder="Email" required
               value="<c:out value='${adherent.email}'/>">
        <input type="text" class="form-control mb-2" name="login" placeholder="Login" required
               value="<c:out value='${adherent.login}'/>">
        <input type="password" class="form-control mb-2" name="motDePasse" minlength="6"
               placeholder="${empty adherent ? 'Mot de passe (6 car. min.)' : 'Nouveau mot de passe (vide = inchangé)'}"
               ${empty adherent ? 'required' : ''}>
        <select class="form-select mb-2" name="role">
          <option value="ADHERENT" ${adherent.role == 'ADHERENT' || empty adherent ? 'selected' : ''}>ADHERENT</option>
          <option value="ADMIN" ${adherent.role == 'ADMIN' ? 'selected' : ''}>ADMIN</option>
        </select>
        <button class="btn btn-primary">Enregistrer</button>
        <c:if test="${not empty adherent}">
          <a class="btn btn-link" href="${ctx}/admin/adherents">Annuler</a>
        </c:if>
      </form>
    </div>
  </div>
  <div class="col-lg-8">
    <table class="table table-striped bg-white align-middle">
      <thead>
        <tr><th>Nom</th><th>Login</th><th>Email</th><th>Rôle</th><th>Inscrit le</th>
            <th class="text-end">Actions</th></tr>
      </thead>
      <tbody>
      <c:forEach var="a" items="${adherents}">
        <tr>
          <td><c:out value="${a.nom}"/> <c:out value="${a.prenom}"/></td>
          <td><c:out value="${a.login}"/></td>
          <td><c:out value="${a.email}"/></td>
          <td><span class="badge ${a.role == 'ADMIN' ? 'bg-danger' : 'bg-secondary'}">${a.role}</span></td>
          <td>${a.dateInscription}</td>
          <td class="text-end">
            <a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/adherents?edit=${a.id}">Modifier</a>
            <form class="d-inline" method="post" action="${ctx}/admin/adherents"
                  onsubmit="return confirm('Supprimer cet adhérent ?');">
              <input type="hidden" name="action" value="delete">
              <input type="hidden" name="id" value="${a.id}">
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