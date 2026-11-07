<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${resultats.totalPages > 1}">
  <nav>
    <ul class="pagination justify-content-center flex-wrap">
      <c:forEach begin="1" end="${resultats.totalPages}" var="i">
        <li class="page-item ${i == resultats.page ? 'active' : ''}">
          <a class="page-link" href="${pageContext.request.contextPath}${baseUrl}?${qs}page=${i}">${i}</a>
        </li>
      </c:forEach>
    </ul>
  </nav>
</c:if>