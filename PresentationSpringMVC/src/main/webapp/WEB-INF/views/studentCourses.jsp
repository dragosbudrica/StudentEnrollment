<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java"
         contentType="text/html; charset=windows-1256"
         pageEncoding="windows-1256"
%>
<%@ page session="true" %>
<c:set var="role" value="${sessionScope.user.role.roleName}" />
<script>
    var role = '${role}';
</script>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <link rel="stylesheet" href="/css/table.css"/>
    <link rel="stylesheet" href="/css/studentCourses.css"/>
    <script type="text/javascript" src="/js/common.js"></script>
    <script type="text/javascript" src="/js/lectures.js"></script>
    <script type="text/javascript" src="/js/studentCourses.js"></script>
</head>
<body>

<div id="warning">
    <img alt="" src="/resources/images/icon-warning-png-11.png"/>
    <h1></h1>
</div>
<div id="studentCourses">
    <h1>My Courses</h1>
    <table id="grid">
        <thead>
        <tr>
            <th>Course Name</th>
            <th>Category</th>
            <th>Professor</th>
        </tr>
        </thead>
        <tfoot>
        <tr>
            <td colspan="3" id="paginationCourses"></td>
        </tr>
        </tfoot>
        <tbody id="tbodyCourses"></tbody>
    </table>
    <div id="message"></div>
</div>

<div id="courseDetails">
    <h3 id="courseName"></h3>
    <table id="courseDetailsGrid">
        <thead>
        <tr>
            <th>Lecture Name</th>
            <th>PDF</th>
        </tr>
        </thead>
        <tfoot>
        <tr>
            <td colspan="2" id="paginationLectures"></td>
        </tr>
        </tfoot>
        <tbody id="tbodyLectures"></tbody>
    </table>
</div>
</body>
</html>
