<%@ page language="java"
         contentType="text/html; charset=windows-1256"
         pageEncoding="windows-1256"
%>
<%@ page isELIgnored="false" %>
<%@ page session="true" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="role" value="${sessionScope.user.role.roleName}" />
<script>
    var role = '${role}';
</script>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <link rel="stylesheet" href="/css/addNewCourse.css"/>
    <link rel="stylesheet" href="/css/table.css"/>
    <link rel="stylesheet" href="/css/professorCourses.css"/>
    <script type="text/javascript" src="/js/common.js"></script>
    <script type="text/javascript" src="/js/addNewCourse.js"></script>
    <script type="text/javascript" src="/js/lectures.js"></script>
    <script type="text/javascript" src="/js/professorCourses.js"></script>
</head>
<body>
<div id="warning">
    <img alt="" src="/resources/images/icon-warning-png-11.png"/>
    <h1></h1>
</div>
<div id="professorCourses">
    <h1>My Courses</h1>
    <table id="coursesGrid">
        <thead>
        <tr>
            <th>Course Name</th>
            <th>Category</th>
            <th></th>
            <th>
                <button id="addCourse"><img src="/resources/images/rsz_add-128.png"></button>
            </th>
        </tr>
        </thead>
        <tfoot>
            <tr>
                <td colspan="4" id="paginationCourses"></td>
            </tr>
        </tfoot>
        <tbody id="tbodyCourses"></tbody>
    </table>
</div>

<form id="fileForm">
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
</form>
<div id="result"></div>

<div id="deleteDialog">
    <span class="close">&times;</span>
    <p>Are you sure you want to delete this course?</p>
    <div class="options">
        <button id="delete" data-courseCode="">Delete</button>
        <button class="cancel">Cancel</button>
    </div>
</div>

<div id="addDialog">
    <span class="close">&times;</span>
    <jsp:include page="fragments/addNewCourseFragm.jsp"/>
</div>
<div class="cover"></div>
</body>
</html>