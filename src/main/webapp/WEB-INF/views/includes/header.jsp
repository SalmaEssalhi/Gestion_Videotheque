<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${empty param.titre ? 'Vidéothèque' : param.titre} - Vidéothèque</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/catalogue">Vidéothèque</a>
        <div class="ms-auto d-flex gap-2">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <span class="navbar-text text-white me-2">
                        ${sessionScope.user.prenom} ${sessionScope.user.nom}
                    </span>
                    <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/logout">Déconnexion</a>
                </c:when>
                <c:otherwise>
                    <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/login">Connexion</a>
                    <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/inscription">Inscription</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</nav>
