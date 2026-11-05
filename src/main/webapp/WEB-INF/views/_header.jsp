<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title><c:out value="${param.titre}"/> - Vidéothèque</title>
  <link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
<div class="navbar">
  <div class="container">
    <a class="brand" href="${ctx}/catalogue">Vidéothèque</a>
    <c:if test="${sessionScope.user.role == 'ADMIN'}">
      <a href="${ctx}/admin/categories">Catégories</a>
      <a href="${ctx}/admin/films">Films</a>
    </c:if>
    <span class="spacer"></span>
    <c:choose>
      <c:when test="${not empty sessionScope.user}">
        <span><c:out value="${sessionScope.user.prenom}"/></span>
        <a href="${ctx}/logout">Déconnexion</a>
      </c:when>
      <c:otherwise>
        <a href="${ctx}/login">Connexion</a>
        <a href="${ctx}/inscription">Inscription</a>
      </c:otherwise>
    </c:choose>
  </div>
</div>
<div class="container">
<c:if test="${not empty sessionScope.flashOk}">
  <div class="alert alert-ok"><c:out value="${sessionScope.flashOk}"/></div>
  <c:remove var="flashOk" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.flashErr}">
  <div class="alert alert-error"><c:out value="${sessionScope.flashErr}"/></div>
  <c:remove var="flashErr" scope="session"/>
</c:if>