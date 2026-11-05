<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/_header.jsp">
    <jsp:param name="titre" value="Connexion"/>
</jsp:include>

<div class="card" style="max-width:440px;margin:40px auto;">
    <h2 style="text-align:center;">Connexion</h2>

    <c:if test="${not empty erreur}">
        <div class="alert alert-error"><c:out value="${erreur}"/></div>
    </c:if>
    <c:if test="${param.inscrit == '1'}">
        <div class="alert alert-ok">Compte créé avec succès. Vous pouvez vous connecter.</div>
    </c:if>
    <c:if test="${param.deconnecte == '1'}">
        <div class="alert alert-ok">Vous avez été déconnecté.</div>
    </c:if>

    <form action="${ctx}/login" method="post">
        <label for="login">Login</label>
        <input type="text" id="login" name="login" value="<c:out value='${login}'/>" required autofocus>

        <label for="motDePasse">Mot de passe</label>
        <input type="password" id="motDePasse" name="motDePasse" required>

        <button type="submit" class="btn" style="width:100%;">Se connecter</button>
    </form>

    <p style="text-align:center;margin-top:16px;">
        Pas de compte ? <a href="${ctx}/inscription">S'inscrire</a>
    </p>
</div>

<jsp:include page="/WEB-INF/views/_footer.jsp"/>