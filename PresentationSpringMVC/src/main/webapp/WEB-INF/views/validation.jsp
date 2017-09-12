<%--
  Created by IntelliJ IDEA.
  User: Dragos
  Date: 18.08.2017
  Time: 11:57
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
    <link rel="stylesheet" href="/css/validation.css"/>
    <script type="text/javascript" src="/js/common.js"></script>
    <script type="text/javascript" src="/js/validation.js"></script>
</head>
<body>
<div id="warning">
    <img alt="" src="/resources/images/icon-warning-png-11.png"/>
    <h1></h1>
</div>
<div id="allCourses">
    <h1>All Courses</h1>
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

<div id="warning2">
    <h1></h1>
</div>

<div id="validation" data-courseCode="">
    <div id="aboveTable">
        <h3 id="courseTitle"></h3>
        <div id="validate">
            <button type="button" id="validateBtn"><img src="/resources/images/rsz_1thumbs_up.png"></button>
            <button type="button" id="invalidateBtn"><img src="/resources/images/rsz_1thumbs_down.png"></button>
        </div>
    </div>
    <table id="validationGrid">
        <thead>
        <tr>
            <th>
                <label for="options">Options</label>
                <select id="options" name="options" onchange="Validation.executeAction()">
                    <option value="Choose An Option">Choose An Option</option>
                    <option value="Select All">Select All</option>
                    <option value="Deselect All">Deselect All</option>
                    <option value="Select Only Absents">Select Only Absents</option>
                    <option value="Select Only Grades">Select Only Grades</option>
                    <option value="Select Validated Results">Select Validated Results</option>
                    <option value="Select Invalidated Results">Select Invalidated Results</option>
                </select>
            </th>
            <th>Last Name</th>
            <th>First Name</th>
            <th>Grade</th>
            <th>Validated</th>
        </tr>
        </thead>
        <tfoot>
        <tr>
            <td colspan="5" id="paginationValidation"></td>
        </tr>
        </tfoot>
        <tbody id="tbodyValidation"></tbody>
    </table>
</div>
</body>
</html>
