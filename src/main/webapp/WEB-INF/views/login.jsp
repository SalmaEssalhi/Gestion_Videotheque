<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/_header.jsp">
    <jsp:param name="titre" value="Connexion"/>
</jsp:include>

<div class="card shadow-sm mx-auto mt-4" style="max-width:440px;">
  <div class="card-body p-4">
    <h1 class="h4 mb-4 text-center">Connexion</h1>

    <c:if test="${not empty erreur}">
      <div class="alert alert-danger"><c:out value="${erreur}"/></div>
    </c:if>
    <c:if test="${param.inscrit == '1'}">
      <div class="alert alert-success">Compte créé avec succès. Vous pouvez vous connecter.</div>
    </c:if>
    <c:if test="${param.deconnecte == '1'}">
      <div class="alert alert-info">Vous avez été déconnecté.</div>
    </c:if>

    <form action="${ctx}/login" method="post">
      <div class="mb-3">
        <label for="login" class="form-label">Login</label>
        <input type="text" class="form-control" id="login" name="login"
               value="<c:out value='${login}'/>" required autofocus>
      </div>
      <div class="mb-3">
        <label for="motDePasse" class="form-label">Mot de passe</label>
        <input type="password" class="form-control" id="motDePasse" name="motDePasse" required>
      </div>
      <button type="submit" class="btn btn-primary w-100">Se connecter</button>
    </form>

    <p class="text-center mt-3 mb-0">
      Pas de compte ? <a href="${ctx}/inscription">S'inscrire</a>
    </p>
  </div>
</div>

<jsp:include page="/WEB-INF/views/_footer.jsp"/>