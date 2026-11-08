<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title><c:out value="${param.titre}"/> - Vidéothèque</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-md navbar-dark bg-dark mb-4">
  <div class="container">
    <a class="navbar-brand" href="${ctx}/catalogue">Vidéothèque</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#nav">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="nav">
      <ul class="navbar-nav me-auto">
        <c:if test="${sessionScope.user.role == 'ADMIN'}">
          <li class="nav-item"><a class="nav-link" href="${ctx}/admin/categories">Catégories</a></li>
          <li class="nav-item"><a class="nav-link" href="${ctx}/admin/films">Films</a></li>
        </c:if>
      </ul>
      <ul class="navbar-nav">
        <c:choose>
          <c:when test="${not empty sessionScope.user}">
            <li class="nav-item"><a class="nav-link" href="${ctx}/mes-emprunts">Mes emprunts</a></li>
            <li class="nav-item"><span class="navbar-text mx-3"><c:out value="${sessionScope.user.prenom}"/></span></li>
            <li class="nav-item"><a class="nav-link" href="${ctx}/logout">Déconnexion</a></li>
          </c:when>
          <c:otherwise>
            <li class="nav-item"><a class="nav-link" href="${ctx}/login">Connexion</a></li>
            <li class="nav-item"><a class="nav-link" href="${ctx}/inscription">Inscription</a></li>
          </c:otherwise>
        </c:choose>
      </ul>
    </div>
  </div>
</nav>
<div class="container">
<c:if test="${not empty sessionScope.flashOk}">
  <div class="alert alert-success"><c:out value="${sessionScope.flashOk}"/></div>
  <c:remove var="flashOk" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.flashErr}">
  <div class="alert alert-danger"><c:out value="${sessionScope.flashErr}"/></div>
  <c:remove var="flashErr" scope="session"/>
</c:if>