<%--
  Created by IntelliJ IDEA.
  User: Dragos
  Date: 28.07.2017
  Time: 17:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java"
         contentType="text/html; charset=windows-1256"
         pageEncoding="windows-1256"
%>
<%@ page session="true" %>
<!DOCTYPE html>

<html>
<head>
    <link rel="stylesheet" href="/css/addNewAccount.css"/>
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="/js/addNewAccount.js"></script>
</head>
<body>
<div class="module form-module">
    <div class="toggle">
    </div>
    <div class="form">
        <h2>Create an account</h2>
        <form id="newAccount">
            <!--First Name-->
            <div class="input_with_error">
                <label for="newAccount_firstName">First Name</label>
                <input type="text" id="newAccount_firstName" name="firstName"/>
                <span class="error">This field is required</span>
            </div>

            <!--Last Name-->
            <div class="input_with_error">
                <label for="newAccount_lastName">Last Name</label>
                <input type="text" id="newAccount_lastName" name="lastName"/>
                <span class="error">This field is required</span>
            </div>

            <!--SSN-->
            <div class="input_with_error">
                <label for="newAccount_ssn">SSN</label>
                <input type="text" id="newAccount_ssn" name="ssn"/>
                <span class="error">A valid SSN is required</span>
            </div>

            <!--Email-->
            <div class="input_with_error">
                <label for="newAccount_email">Email</label>
                <input type="text" id="newAccount_email" name="email"/>
                <span class="error">A valid email address is required</span>
            </div>

            <!--Password-->
            <div class="input_with_error">
                <label for="newAccount_password">Password</label>
                <input type="password" id="newAccount_password" name="password"/>
                <span class="error">This field is required</span>
            </div>

            <label for="role">Role</label>
            <select id="role" name="role">
                <option value="Professor">Professor</option>
                <option value="Student">Student</option>
            </select>
            <button type="button" id="submit">Register</button>
        </form>
    </div>
    <div id="message"></div>
</div>
</body>
</html>
