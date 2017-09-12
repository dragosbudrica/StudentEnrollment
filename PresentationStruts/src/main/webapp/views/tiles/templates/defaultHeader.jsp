<%@ page isELIgnored="false" %>
<%@ page session="true" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="firstName" value="${sessionScope.user.firstName}" />
<c:if test="${not empty sessionScope.user}">
    <form action="logout" method="post">
        <h2 style="display: inline; margin-left: 10px"><c:out value="Welcome, ${firstName}!" /> </h2>
        <button style="display: inline; margin-right: 20px; right: 0; position: absolute" id=logout><img src="/resources/images/rsz_56805.png"></button>
    </form>
</c:if>
