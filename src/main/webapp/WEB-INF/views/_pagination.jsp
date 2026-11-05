<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${resultats.totalPages > 1}">
  <div class="pagination">
    <c:forEach begin="1" end="${resultats.totalPages}" var="i">
      <c:choose>
        <c:when test="${i == resultats.page}"><span class="active">${i}</span></c:when>
        <c:otherwise><a href="${pageContext.request.contextPath}${baseUrl}?${qs}page=${i}">${i}</a></c:otherwise>
      </c:choose>
    </c:forEach>
  </div>
</c:if>