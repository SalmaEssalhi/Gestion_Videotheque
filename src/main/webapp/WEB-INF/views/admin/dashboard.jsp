<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/_header.jsp"><jsp:param name="titre" value="Tableau de bord"/></jsp:include>

<h2 class="mb-4">Tableau de bord</h2>
<div class="row g-3">
  <div class="col-md-3"><div class="card text-center"><div class="card-body">
    <div class="fs-2">${nbFilms}</div><div class="text-muted">Films</div></div></div></div>
  <div class="col-md-3"><div class="card text-center"><div class="card-body">
    <div class="fs-2">${nbUtilisateurs}</div><div class="text-muted">Utilisateurs</div></div></div></div>
  <div class="col-md-3"><div class="card text-center"><div class="card-body">
    <div class="fs-2">${enCours}</div><div class="text-muted">Emprunts en cours</div></div></div></div>
  <div class="col-md-3"><div class="card text-center"><div class="card-body">
    <div class="fs-2"><fmt:formatNumber value="${chiffreAffaires}" minFractionDigits="2"/> MAD</div>
    <div class="text-muted">Chiffre d'affaires (simulé)</div></div></div></div>
</div>
<div class="row g-3 mt-1">
  <div class="col-md-3"><div class="card text-center"><div class="card-body">
    <div class="fs-4">${enAttente}</div><div class="text-muted">En attente de paiement</div></div></div></div>
  <div class="col-md-3"><div class="card text-center"><div class="card-body">
    <div class="fs-4">${expires}</div><div class="text-muted">Expirés</div></div></div></div>
  <div class="col-md-3"><div class="card text-center"><div class="card-body">
    <div class="fs-4">${rendus}</div><div class="text-muted">Rendus</div></div></div></div>
  <div class="col-md-3"><div class="card text-center"><div class="card-body">
    <div class="fs-4">${annules}</div><div class="text-muted">Annulés</div></div></div></div>
</div>

<jsp:include page="/WEB-INF/views/_footer.jsp"/>