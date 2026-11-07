<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/_header.jsp">
    <jsp:param name="titre" value="Inscription"/>
</jsp:include>

<div class="card shadow-sm mx-auto mt-4" style="max-width:560px;">
  <div class="card-body p-4">
    <h1 class="h4 mb-4 text-center">Créer un compte</h1>

    <c:if test="${not empty erreur}">
      <div class="alert alert-danger"><c:out value="${erreur}"/></div>
    </c:if>

    <form action="${ctx}/inscription" method="post">
      <div class="row">
        <div class="col-md-6 mb-3">
          <label for="nom" class="form-label">Nom</label>
          <input type="text" class="form-control" id="nom" name="nom"
                 value="<c:out value='${param.nom}'/>" required>
        </div>
        <div class="col-md-6 mb-3">
          <label for="prenom" class="form-label">Prénom</label>
          <input type="text" class="form-control" id="prenom" name="prenom"
                 value="<c:out value='${param.prenom}'/>" required>
        </div>
      </div>
      <div class="mb-3">
        <label for="email" class="form-label">Email</label>
        <input type="email" class="form-control" id="email" name="email"
               value="<c:out value='${param.email}'/>" required>
      </div>
      <div class="mb-3">
        <label for="login" class="form-label">Login</label>
        <input type="text" class="form-control" id="login" name="login"
               value="<c:out value='${param.login}'/>" required>
      </div>
      <div class="row">
        <div class="col-md-6 mb-3">
          <label for="motDePasse" class="form-label">Mot de passe</label>
          <input type="password" class="form-control" id="motDePasse" name="motDePasse" minlength="6" required>
        </div>
        <div class="col-md-6 mb-3">
          <label for="confirmation" class="form-label">Confirmation</label>
          <input type="password" class="form-control" id="confirmation" name="confirmation" minlength="6" required>
        </div>
      </div>
      <button type="submit" class="btn btn-primary w-100">S'inscrire</button>
    </form>

    <p class="text-center mt-3 mb-0">
      Déjà inscrit ? <a href="${ctx}/login">Se connecter</a>
    </p>
  </div>
</div>

<jsp:include page="/WEB-INF/views/_footer.jsp"/>