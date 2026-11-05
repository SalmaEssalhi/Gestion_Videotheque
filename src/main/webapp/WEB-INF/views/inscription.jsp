<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/_header.jsp">
    <jsp:param name="titre" value="Inscription"/>
</jsp:include>

<div class="card" style="max-width:560px;margin:40px auto;">
    <h2 style="text-align:center;">Créer un compte</h2>

    <c:if test="${not empty erreur}">
        <div class="alert alert-error"><c:out value="${erreur}"/></div>
    </c:if>

    <form class="form-grid" action="${ctx}/inscription" method="post">
        <div>
            <label for="nom">Nom</label>
            <input type="text" id="nom" name="nom" value="<c:out value='${param.nom}'/>" required>
        </div>
        <div>
            <label for="prenom">Prénom</label>
            <input type="text" id="prenom" name="prenom" value="<c:out value='${param.prenom}'/>" required>
        </div>
        <div class="full">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" value="<c:out value='${param.email}'/>" required>
        </div>
        <div class="full">
            <label for="login">Login</label>
            <input type="text" id="login" name="login" value="<c:out value='${param.login}'/>" required>
        </div>
        <div>
            <label for="motDePasse">Mot de passe</label>
            <input type="password" id="motDePasse" name="motDePasse" minlength="6" required>
        </div>
        <div>
            <label for="confirmation">Confirmation</label>
            <input type="password" id="confirmation" name="confirmation" minlength="6" required>
        </div>
        <div class="full">
            <button type="submit" class="btn" style="width:100%;">S'inscrire</button>
        </div>
    </form>

    <p style="text-align:center;margin-top:16px;">
        Déjà inscrit ? <a href="${ctx}/login">Se connecter</a>
    </p>
</div>

<jsp:include page="/WEB-INF/views/_footer.jsp"/>