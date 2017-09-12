var Grades = Grades || {};

$(document).ready(function () {
    Grades.getCoursesWithStudents();
});

Grades.getCoursesWithStudents = function getCoursesWithStudents() {
    var warning = $("#warning");
    $.ajax({
        url: '/getCoursesWithEnrolledStudents',
        type: 'GET',
        dataType: 'json',
        traditional: true,
        success: function (data) {
            if (data.length === 0) {
                $(warning).find('h1').text("You have't added any course yet!");
                warning.show();
            } else {
                Utils.sortCourseAscending(data);
                Grades.renderCourses(data);
                Grades.prepareDisplayCourseStudents();
            }
        },
        error: function (data) {
            console.log(data);
        }
    });
};

Grades.renderCourses = function renderCourses(data) {
    var tbodyCourses = $('#myCoursesBody');
    var paginationCourses = $('#paginationCourses');
    var tr;
    for (var i = 0; i < data.length; i++) {
        tr = $('<tr/>');
        tr.append("<td><span data-courseCode=" + data[i].courseCode + " class=\"courseName\">" + data[i].courseName + "</span></td>");
        tr.append("<td>" + data[i].category + "</td>");
        tr.append("<td>" + data[i].numberOfEnrolledStudents + " enrolled student(s)</td>");
        tbodyCourses.append(tr);
    }
    $('#myCourses').show();
    Utils.pagination(tbodyCourses, paginationCourses);
};

Grades.prepareDisplayCourseStudents = function prepareDisplayCourseStudents() {
    var spansCourseName = $('#coursesGrid').find("span[class=courseName]");

    for (var i = 0; i < spansCourseName.length; i++) {
        $(spansCourseName[i]).on("click", Grades.getEnrolledStudents);
    }
};

Grades.getEnrolledStudents = function getEnrolledStudents() {
    $('#tbodyGrades').empty();
    $('#paginationGrades').empty();
    var grades = $('#grades');
    var warning2 = $('#warning2');
    var courseCode = $(this).attr('data-courseCode');
    $('#courseTitle').text($(this).text());
    grades.attr('data-courseCode', courseCode);

    var jsonParam = JSON.stringify({
        'courseCode': courseCode
    });
    $.ajax({
        url: "/getEnrolledStudents",
        type: 'POST',
        data: jsonParam,
        contentType: "application/json",
        success: function (data) {
            if(data.length === 0) {
                grades.hide();
                $(warning2).find('h1').text("There are no enrolled students at this course yet!");
                warning2.show();
            } else {
                warning2.hide();
                Utils.sortStudentsAscending(data);
                Grades.renderStudents(data);
            }
        },
        error: function (data) {
            console.log(data);
        }
    });
};

Grades.renderStudents = function renderStudents(data) {
    var tbodyGrades = $('#tbodyGrades');
    var paginationGrades = $('#paginationGrades');
    var tr;
    var result;
    var validated;
    for (var i = 0; i < data.length; i++) {
        tr = $('<tr/>');
        result = data[i].result;
        validated = data[i].validated;
        if (result === null) {
            result = noGrade;
        }
        tr.append("<td>" + (i + 1) + "</td>");
        tr.append("<td>" + data[i].lastName + "</td>");
        tr.append("<td>" + data[i].firstName + "</td>");
        tr.append("<td><span>" + result + "</span><select class=\"result\" name=\"result\"></select></td>");
        if (!validated) {
            tr.append("<td><button class=\"editResult saveResult\" data-userId="+ data[i].userId +" ><img src=\"/resources/images/rsz_rsz_edit-pencil-_write_-128.png\"></button></td>");
            tr.append("<td><button class=\"remove cancelEditing\" data-userId="+ data[i].userId +"><img src=\"/resources/images/rsz_rsz_61848.png\"></button></td>");
        }
        tbodyGrades.append(tr);

    }
    $('#grades').show();
    Utils.pagination(tbodyGrades, paginationGrades);
    Grades.prepareEditing();
    Grades.prepareRemoval();
    Grades.checkRemoveDialogActions();
};

Grades.prepareEditing = function prepareEditing() {
    var tbody = $("#tbodyGrades");
    var editButtons = $(tbody).find("button[class^=editResult]");

    for (var i = 0; i < editButtons.length; i++) {
        $(editButtons[i]).on("click", Grades.editResult);
    }
};

Grades.editResult = function editResult() {
    // add edit class on result td
    if ($(this).hasClass('editResult')) {
        Grades.fillDropdownWithResults(this);
    }
    else {
        Grades.updateResult(this);
    }
};

Grades.updateResult = function updateResult(saveButton) {
    // set span based on option value
    var resultDropDown = $(saveButton).parent().parent().children(':nth-child(4)').find('select');
    var spanResultToEdit = $(resultDropDown).prev();
    $(spanResultToEdit).text($(resultDropDown).val());

    $(saveButton).parent().parent().children(":nth-child(4)").removeClass("edit");

    Grades.toggleClassesAndIconsBackToNormal(saveButton);
    Grades.enableTheOtherButtons();
    Grades.sendEditingAjax(saveButton);
};

Grades.fillDropdownWithResults = function fillDropdownWithResults(editButton) {
    var dropDown = $(editButton).parent().parent().children(":nth-child(4)");
    var absent = "<option value=\"Absent\">Absent</option>";
    $(absent).appendTo('.result');

    for (index = 1; index <= 10; ++index) {
        var result = "<option value=" + index + ">" + index + "</option>";
        $(result).appendTo('.result');
    }
    $(dropDown).addClass("edit");
    var select = $(dropDown).find("select");
    select.focus();

    Grades.toggleClassesAndIcons(editButton);
};

Grades.toggleClassesAndIcons = function toggleClassesAndIcons(button) {
    // toggle classes
    var removeCancelButton = $(button).parent().next().children(":first-child");
    $(button).toggleClass('editResult');
    removeCancelButton.toggleClass('remove');

    // change icons
    $(button).children(":first-child").attr("src", "/resources/images/rsz_artboard_73-128.png");
    removeCancelButton.find("img").attr("src", "/resources/images/rsz_no-128.png");

    Grades.disableTheOtherButtons();
};

Grades.disableTheOtherButtons = function disableTheOtherButtons() {
    // disable the other buttons
    var tBodyGrades = $('#tbodyGrades');
    var otherEditButtons = tBodyGrades.find("button[class^=editResult]");
    var otherRemoveButtons = tBodyGrades.find("button[class^=remove]");

    for (var i = 0; i < otherEditButtons.length; i++) {
        otherEditButtons[i].setAttribute("disabled", true);
        otherRemoveButtons[i].setAttribute("disabled", true);
    }
};

Grades.toggleClassesAndIconsBackToNormal = function toggleClassesAndIconsBackToNormal(button) {
    // toggle classes
    var editSaveButton, removeCancelButton;
    if ($(button).parent().next('td').length === 0) {
        editSaveButton = $(button).parent().prev().children(":first-child");
        $(button).removeClass('cancelEditing').addClass('remove cancelEditing');
        editSaveButton.removeClass('saveResult').addClass('editResult saveResult');

        // change icons
        $(button).children(":first-child").attr("src", "/resources/images/rsz_rsz_61848.png");
        editSaveButton.find("img").attr("src", "/resources/images/rsz_rsz_edit-pencil-_write_-128.png");
    } else {
        removeCancelButton = $(button).parent().next().children(":first-child");
        $(button).removeClass('saveResult').addClass('editResult saveResult');
        removeCancelButton.removeClass('cancelEditing').addClass('remove cancelEditing');

        // change icons
        $(button).children(":first-child").attr("src", "/resources/images/rsz_rsz_edit-pencil-_write_-128.png");
        removeCancelButton.find("img").attr("src", "/resources/images/rsz_rsz_61848.png");
    }
};

Grades.enableTheOtherButtons = function enableTheOtherButtons() {
    // enable the other buttons
    var theGrid = $('#gradesGrid');
    var otherButtons = theGrid.find("button[disabled=true]");
    for (var i = 0; i < otherButtons.length; i++) {
        $(otherButtons[i]).removeAttr("disabled");
    }
};

Grades.sendEditingAjax = function sendEditingAjax(saveButton) {
    var spanResult = $(saveButton).parent().parent().children(':nth-child(4)').find('span');
    var jsonParams = JSON.stringify({
        'courseCode': $('#grades').attr('data-courseCode'),
        'userId': $(saveButton).attr('data-userId'),
        'result': $(spanResult).text()
    });

    // empty the list
    $('.result').empty();

    // send request
    $.ajax({
        url: '/editResult',
        type: 'PUT',
        data: jsonParams,
        contentType: 'application/json',
        traditional: true,
        success: function (data) {
            console.log("Success editing");
        },
        error: function (data) {
        }
    });
};

Grades.cancelEditing = function cancelEditing(cancelButton) {
    // remove edit class from td course name
    $('#tbodyGrades').find('td[class=edit]').removeClass('edit');

    Grades.toggleClassesAndIconsBackToNormal(cancelButton);
    Grades.enableTheOtherButtons();

    // empty the list
    $('.result').empty();
};

Grades.prepareRemoval = function prepareRemoval() {
    var tbody = $("#tbodyGrades");
    var removeButtons = $(tbody).find("button[class^=remove]");

    for (var i = 0; i < removeButtons.length; i++) {
        $(removeButtons[i]).on("click", Grades.removeResult);
    }
};

Grades.removeResult = function removeResult() {
    var removeButton = $("#remove");
    // remove result
    if ($(this).hasClass('remove')) {
        $(".cover").fadeIn('slow');
        $("#removeDialog").fadeIn('slow');
        $(removeButton).attr('data-userId', $(this).attr('data-userId'));
        $(removeButton).attr('data-courseCode', $('#grades').attr('data-courseCode'));
    } else {
        Grades.cancelEditing(this);
    }
};

Grades.checkRemoveDialogActions = function checkRemoveDialogActions() {
    $("#removeDialog").on('click', function () {
        if ($(event.target).is(".close") || $(event.target).is(".cancel")) {
            $(".cover").fadeOut('slow');
            $("#removeDialog").fadeOut('slow');
        } else if ($(event.target).is("#remove")) {
            var removeButton = $("#remove");
            var courseCode = removeButton.attr('data-courseCode');
            var userId = removeButton.attr('data-userId');

            Grades.sendRemovalAjax(courseCode, userId);
        }
    });
};

Grades.sendRemovalAjax = function sendRemovalAjax(courseCode, userId) {
    var jsonParams = JSON.stringify({
        'courseCode': courseCode,
        'userId': userId
    });

    $.ajax({
        url: '/removeResult',
        type: 'DELETE',
        data: jsonParams,
        contentType: 'application/json',
        traditional: true,
        success: function (data) {
           var result = $('#tbodyGrades').find("button[data-userId="+userId+"]").parent().parent().children(':nth-child(4)').find('span');
           $(result).text(noGrade);
            $(".cover").fadeOut('slow');
            $("#removeDialog").fadeOut('slow');
        },
        error: function (data) {
            $(".cover").fadeOut('slow');
            $("#removeDialog").fadeOut('slow');
        }
    });
};



