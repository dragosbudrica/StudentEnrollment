var ProfessorCourses = ProfessorCourses || {};

$(document).ready(function () {
    ProfessorCourses.getProfessorCourses();
});

ProfessorCourses.getProfessorCourses = function getProfessorCourses() {
    var warning = $("#warning");
    $.ajax({
        url: '/getProfessorCourses',
        type: 'GET',
        dataType: 'json',
        traditional: true,
        success: function (data) {
            if(data.length === 0) {
                $(warning).find('h1').text("You have't added any course yet!");
                warning.show();
            } else {
                Utils.sortCourseAscending(data);
                ProfessorCourses.renderCourses(data);
                ProfessorCourses.addCourse();
                Utils.prepareDisplayCourseDetails();
                ProfessorCourses.prepareEditing();
                ProfessorCourses.prepareRemoval();
                ProfessorCourses.checkDeleteDialogActions();
                ProfessorCourses.checkAddDialogActions();
            }
        },
        error: function (data) {
            console.log(data);
        }
    });
};

ProfessorCourses.renderCourses = function renderCourses(data) {
    var tbodyCourses = $('#tbodyCourses');
    var paginationCourses = $('#paginationCourses');
    var tr;
    for (var i = 0; i < data.length; i++) {
        tr = $('<tr/>');
        tr.append("<td><span data-courseCode=" + data[i].courseCode + " class=\"courseNameClass\" style='cursor:pointer'>" + data[i].courseName +
            "</span><input class='courseName' value=" + data[i].courseName + "></td>");
        tr.append("<td><span>" + data[i].category + "</span><select class=\"category\" name=\"category\"></select></td>");
        tr.append("<td><button class=\"editCourse saveCourse\" value=" + data[i].courseCode + "><img src=\"/resources/images/rsz_rsz_edit-pencil-_write_-128.png\"></button></td>");
        tr.append("<td><button class=\"remove cancelEditing\" value=" + data[i].courseCode + " ><img src=\"/resources/images/rsz_rsz_61848.png\"></button></td>");
        $(tbodyCourses).append(tr);
    }
    $('#professorCourses').show();
    Utils.pagination(tbodyCourses, paginationCourses);
};

ProfessorCourses.addCourse = function addCourse() {
    $('#addCourse').click(function () {
        $(".cover").fadeIn('slow');
        $("#addDialog").fadeIn('slow');
    });
};

ProfessorCourses.checkAddDialogActions = function checkAddDialogActions() {
    $("#addDialog").on('click', function () {
        if ($(event.target).is(".close")) {
            $(".cover").fadeOut('slow');
            $("#addDialog").fadeOut('slow');
            window.location.reload();
        }
    });
};

ProfessorCourses.prepareEditing = function prepareEditing() {
    var tbody = $("#tbodyCourses");
    var editButtons = $(tbody).find("button[class^=editCourse]");

    for (var i = 0; i < editButtons.length; i++) {
        $(editButtons[i]).on("click", ProfessorCourses.editCourse);
    }
};

ProfessorCourses.editCourse = function editCourse() {
    // add edit class on course name td
    if ($(this).hasClass('editCourse')) {
        ProfessorCourses.getAllCategories(this);
    }
    else {
        ProfessorCourses.updateCourse(this);
    }
};

ProfessorCourses.getAllCategories = function getAllCategories(editButton) {
    $.ajax({
        url: '/getAllCategories',
        type: 'GET',
        dataType: 'json',
        traditional: true,
        success: function (data) {
            console.log(data);
            for (index = 0; index < data.length; ++index) {
                var category = "<option value=" + data[index].categoryName + ">" + data[index].categoryName + "</option>";
                $(category).appendTo('.category');
            }
            $(editButton).parent().parent().children(":first-child").addClass("edit");
            $(editButton).parent().parent().children(":nth-child(2)").addClass("edit");
            var input = $(editButton).parent().parent().children(":first-child").find("input");
            input.focus();
            ProfessorCourses.toggleClassesAndIcons(editButton);

        },
        error: function (data) {
            console.log(data);
        }
    });
};

ProfessorCourses.updateCourse = function updateCourse(saveButton) {
    // set span based on input value
    var courseNameInput = $(saveButton).parent().parent().children(':first-child').find('input');
    var spanCourseNameToEdit = $(courseNameInput).prev();
    $(spanCourseNameToEdit).text($(courseNameInput).val());

    // set span based on option value
    var categoryDropDown = $(saveButton).parent().parent().children(':nth-child(2)').find('select');
    var spanCategoryToEdit = $(categoryDropDown).prev();
    $(spanCategoryToEdit).text($(categoryDropDown).val());

    // remove edit class from course name td
    $(saveButton).parent().parent().children(":first-child").removeClass("edit");
    $(saveButton).parent().parent().children(":nth-child(2)").removeClass("edit");

    ProfessorCourses.toggleClassesAndIconsBackToNormal(saveButton);
    ProfessorCourses.enableTheOtherButtons();
    ProfessorCourses.sendEditingAjax(saveButton);
};

ProfessorCourses.toggleClassesAndIcons = function toggleClassesAndIcons(button) {
    // toggle classes
    var removeCancelButton = $(button).parent().next().children(":first-child");
    $(button).toggleClass('editCourse');
    removeCancelButton.toggleClass('remove');

    // change icons
    $(button).children(":first-child").attr("src", "/resources/images/rsz_artboard_73-128.png");
    removeCancelButton.find("img").attr("src", "/resources/images/rsz_no-128.png");

    ProfessorCourses.disableTheOtherButtons();
};

ProfessorCourses.disableTheOtherButtons = function disableTheOtherButtons() {
    // disable the other buttons
    var theGrid = $('#coursesGrid');
    var otherEditButtons = theGrid.find("button[class^=editCourse]");
    var otherRemoveButtons = theGrid.find("button[class^=remove]");
    for (var i = 0; i < otherEditButtons.length; i++) {
        otherEditButtons[i].setAttribute("disabled", true);
        otherRemoveButtons[i].setAttribute("disabled", true);
    }
};

ProfessorCourses.cancelEditing = function cancelEditing(cancelButton) {
    // remove edit class from td course name
    $('#tbodyCourses').find('td[class=edit]').removeClass('edit');

    ProfessorCourses.toggleClassesAndIconsBackToNormal(cancelButton);
    ProfessorCourses.enableTheOtherButtons();

    // empty the list
    $('.category').empty();
};

ProfessorCourses.toggleClassesAndIconsBackToNormal = function toggleClassesAndIconsBackToNormal(button) {
    // toggle classes
    var editSaveButton, removeCancelButton;
    if ($(button).parent().next('td').length === 0) {
        editSaveButton = $(button).parent().prev().children(":first-child");
        $(button).removeClass('cancelEditing').addClass('remove cancelEditing');
        editSaveButton.removeClass('saveCourse').addClass('editCourse saveCourse');

        // change icons
        $(button).children(":first-child").attr("src", "/resources/images/rsz_rsz_61848.png");
        editSaveButton.find("img").attr("src", "/resources/images/rsz_rsz_edit-pencil-_write_-128.png");
    } else {
        removeCancelButton = $(button).parent().next().children(":first-child");
        $(button).removeClass('saveCourse').addClass('editCourse saveCourse');
        removeCancelButton.removeClass('cancelEditing').addClass('remove cancelEditing');

        // change icons
        $(button).children(":first-child").attr("src", "/resources/images/rsz_rsz_edit-pencil-_write_-128.png");
        removeCancelButton.find("img").attr("src", "/resources/images/rsz_rsz_61848.png");
    }
};

ProfessorCourses.enableTheOtherButtons = function enableTheOtherButtons() {
    // enable the other buttons
    var theGrid = $('#coursesGrid');
    var otherButtons = theGrid.find("button[disabled=true]");
    for (var i = 0; i < otherButtons.length; i++) {
        $(otherButtons[i]).removeAttr("disabled");
    }
};

ProfessorCourses.prepareRemoval = function prepareRemoval() {
    var tbody = $("#tbodyCourses");
    var removeButtons = $(tbody).find("button[class^=remove]");

    for (var i = 0; i < removeButtons.length; i++) {
        $(removeButtons[i]).on("click", ProfessorCourses.removeCourse);
    }
};

ProfessorCourses.removeCourse = function removeCourse() {
    // remove course
    if ($(this).hasClass('remove')) {
        $(".cover").fadeIn('slow');
        $("#deleteDialog").fadeIn('slow');
        $("#delete").attr('data-courseCode', $(this).attr('value'));
    } else {
        ProfessorCourses.cancelEditing(this);
    }
};

ProfessorCourses.sendEditingAjax = function sendEditingAjax(saveButton) {
    var spanCourseName = $(saveButton).parent().parent().children(':first-child').find('span');
    var spanCategory = $(saveButton).parent().parent().children(':nth-child(2)').find('span');
    var jsonParams = JSON.stringify({
        'courseCode': $(saveButton).attr('value'),
        'courseName': $(spanCourseName).text(),
        'category': $(spanCategory).text()
    });

    // empty the list
    $('.category').empty();

    // send request
    $.ajax({
        url: '/editCourse',
        type: 'PUT',
        data: jsonParams,
        contentType: 'application/json',
        traditional: true,
        success: function (data) {
            console.log("Success editing");
        },
        error: function (data) {
            console.log(data);
        }
    });
};

ProfessorCourses.checkDeleteDialogActions = function checkDeleteDialogActions() {
    $("#deleteDialog").on('click', function () {
        if ($(event.target).is(".close") || $(event.target).is(".cancel")) {
            $(".cover").fadeOut('slow');
            $("#deleteDialog").fadeOut('slow');
        } else if ($(event.target).is("#delete")) {
            var deleteButton = $("#delete");
            var courseCode = deleteButton.attr('data-courseCode');
            ProfessorCourses.sendRemovalAjax(courseCode);
        }
    });
};

ProfessorCourses.sendRemovalAjax = function sendRemovalAjax(courseCode) {
    var jsonParam = JSON.stringify({
        'courseCode': courseCode
    });

    $.ajax({
        url: '/deleteCourse',
        type: 'DELETE',
        data: jsonParam,
        contentType: 'application/json',
        traditional: true,
        success: function (data) {
            var trToBeRemoved = $('#tbodyCourses').find("button[value=" + courseCode + "]").parent().parent();
            $(trToBeRemoved).remove();
            $(".cover").fadeOut('slow');
            $("#deleteDialog").fadeOut('slow');
        },
        error: function (data) {
            $(".cover").fadeOut('slow');
            $("#deleteDialog").fadeOut('slow');
        }
    });
};




















