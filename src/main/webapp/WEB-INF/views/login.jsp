<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="titre" value="Connexion"/>
</jsp:include>

<div class="container mt-5" style="max-width: 440px;">
    <div class="card shadow-sm">
        <div class="card-body p-4">
            <h1 class="h4 mb-4 text-center">Connexion</h1>

            <c:if test="${not empty erreur}">
                <div class="alert alert-danger" role="alert"><c:out value="${erreur}"/></div>
            </c:if>
            <c:if test="${param.inscrit == '1'}">
                <div class="alert alert-success" role="alert">Compte créé avec succès. Vous pouvez vous connecter.</div>
            </c:if>
            <c:if test="${param.deconnecte == '1'}">
                <div class="alert alert-info" role="alert">Vous avez été déconnecté.</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="post">
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
                Pas de compte ?
                <a href="${pageContext.request.contextPath}/inscription">S'inscrire</a>
            </p>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
