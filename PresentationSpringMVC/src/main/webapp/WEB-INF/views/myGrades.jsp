<%--
  Created by IntelliJ IDEA.
  User: Dragos
  Date: 16.08.2017
  Time: 17:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<jsp:useBean id="cons" class="com.kepler.rominfo.service.utils.Constants"/>
<script>
    var noGrade = '${cons.NO_GRADE}';
</script>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <link rel="stylesheet" href="/css/table.css"/>
    <link rel="stylesheet" href="/css/myGrades.css"/>
    <script type="text/javascript" src="/js/common.js"></script>
    <script type="text/javascript" src="/js/myGrades.js"></script>
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
            <th>Grade</th>
        </tr>
        </thead>
        <tfoot>
        <tr>
            <td colspan="4" id="paginationCourses"></td>
        </tr>
        </tfoot>
        <tbody id="tbodyCourses"></tbody>
    </table>
    <div id="message"></div>
</div>
</body>
</html>
