<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/_header.jsp"><jsp:param name="titre" value="Paiement"/></jsp:include>

<div class="card shadow-sm mx-auto" style="max-width:480px;">
  <div class="card-body p-4">
    <h1 class="h4 mb-3">Paiement (simulé)</h1>
    <p>
      <strong><c:out value="${emprunt.film.titre}"/></strong><br>
      Montant : <strong><fmt:formatNumber value="${emprunt.film.prixLocation}" minFractionDigits="2"/> MAD</strong>
    </p>
    <div class="alert alert-info small">
      Simulation pédagogique : aucune transaction réelle. Une carte finissant par 0000 est refusée.
    </div>

    <c:if test="${not empty erreur}">
      <div class="alert alert-danger"><c:out value="${erreur}"/></div>
    </c:if>

    <form method="post" action="${ctx}/paiement" autocomplete="off">
      <input type="hidden" name="id" value="${emprunt.id}">
      <div class="mb-3">
        <label class="form-label">Numéro de carte (16 chiffres)</label>
        <input type="text" class="form-control" name="numero" maxlength="19" required>
      </div>
      <div class="row">
        <div class="col-6 mb-3">
          <label class="form-label">Expiration (MM/AA)</label>
          <input type="text" class="form-control" name="expiration" placeholder="12/28" required>
        </div>
        <div class="col-6 mb-3">
          <label class="form-label">CVV</label>
          <input type="password" class="form-control" name="cvv" maxlength="3" required>
        </div>
      </div>
      <button class="btn btn-primary w-100">Payer</button>
    </form>

    <form method="post" action="${ctx}/emprunt" class="mt-2">
      <input type="hidden" name="action" value="annuler">
      <input type="hidden" name="id" value="${emprunt.id}">
      <button class="btn btn-link w-100">Annuler l'emprunt</button>
    </form>
  </div>
</div>

<jsp:include page="/WEB-INF/views/_footer.jsp"/>