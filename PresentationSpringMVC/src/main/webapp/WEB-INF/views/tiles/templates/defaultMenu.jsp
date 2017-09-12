<%@ page isELIgnored="false" %>
<%@ page session="true" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="role" value="${sessionScope.user.role.roleName}"/>
<c:if test="${not empty sessionScope.user}">
    <nav>
        <img class="logo" src="/resources/images/elearning.png"/>
        <ul id="menu">
            <c:if test="${role eq 'Admin'}">
                <li><a href="/newAccount">New Account</a></li>
                <li><a href="/courseScheduling">Scheduling</a></li>
                <li><a href="/validation">Validation</a></li>
            </c:if>

            <c:if test="${role eq 'Professor'}">
                <li><a href="/professorCourses">My Courses</a></li>
                <li><a href="/newCourse">Add Course</a></li>
                <li><a href="/timetable">Timetable</a></li>
                <li><a href="/grades">Grades</a></li>
            </c:if>

            <c:if test="${role eq 'Student'}">
                <li><a href="/allCourses">All Courses</a></li>
                <li><a href="/studentCourses">My Courses</a></li>
                <li><a href="/timetable">Timetable</a></li>
                <li><a href="/myGrades">My Grades</a></li>
            </c:if>
        </ul>
    </nav>
</c:if>