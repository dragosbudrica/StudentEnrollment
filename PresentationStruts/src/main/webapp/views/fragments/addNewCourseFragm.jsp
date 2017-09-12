<%--
  Created by IntelliJ IDEA.
  User: Dragos
  Date: 07.08.2017
  Time: 10:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="module form-module">
    <div class="toggle">
    </div>
    <div class="form">
        <h2>Create a new course</h2>
        <form id="newCourse">
            <!--Course Name-->
            <div class="input_with_error">
                <label for="newCourse_courseName">Course Name</label>
                <input type="text" id="newCourse_courseName" name="courseName"/>
                <span class="error">This field is required</span>
            </div>

            <!--Category-->
            <label for="categoryAddCourse">Category</label>
            <select id="categoryAddCourse" name="categoryAddCourse"></select>

            <!--Number of lectures-->
            <label for="numberOfLectures">Number of lectures</label>
            <input type="number" value="3" min="3" max="12" id="numberOfLectures" name="numberOfLectures"/>
            <span class="error">This field is required</span>

            <!--Description-->
            <div class="input_with_error">
                <label for="newCourse_description">Description</label>
                <textarea id="newCourse_description" rows="5" cols="57" name="description" maxlength="255"
                          style="resize: none" onkeyup="Utils.countChar(this)"></textarea>
                <span class="error">This field is required</span>
                <div id="charNum"></div>
            </div>

            <button type="button" id="submit">Add</button>
        </form>
    </div>
    <div id="message"></div>
</div>