<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="/css/login.css"/>
    <title>Login Page</title>
</head>

<body>

<div class="app-title">
    <h1>E-Learning App.</h1>
</div>
<!-- Form Module-->
<div class="module form-module">
    <div class="toggle">
    </div>
    <div class="form">
        <h2>Login to your account</h2>
        <form:form id="doLogin" modelAttribute="loginDto" action="doLogin" method="post">
            <form:label path="email">Email</form:label>
            <form:input path="email" type="text" id="email" name="email"/> <br/>
            <form:label path="password" for="password">Password</form:label>
            <form:input path="password" type="password" id="password" name="password"/> <br/>
            <form:button id="login" name="login">Login</form:button>
        </form:form>
    </div>
    <c:if test="${not empty message}">
        <div class="errors">${message}</div>
    </c:if>
</div>
</body>
</html>



