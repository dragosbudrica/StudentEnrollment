<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
        <form id="doLogin" action="doLogin" method="post">
            <label for="email">Email</label>
            <input id="email" name="email"/> <br/>
            <label for="password">Password</label>
            <input type="password" id="password" name="password"/> <br/>
            <button id="login" name="login">Login</button>
        </form>
    </div>
    <s:if test="hasActionErrors()">
        <div class="errors">
            <s:actionerror/>
        </div>
    </s:if>
</div>
</body>
</html>



