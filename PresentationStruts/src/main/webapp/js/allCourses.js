var AllCourses = AllCourses || {};

$(document).ready(function () {
    AllCourses.getAllCourses();
});

AllCourses.getAllCourses = function getAllCourses() {
    var warning = $("#warning");
    $.ajax({
        url: '/getAllCourses',
        type: 'GET',
        dataType: 'json',
        traditional: true,
        success: function (data) {
            if (data.allCourses.length === 0) {
                $(warning).find('h1').text("There are no courses added yet!");
                warning.show();
            } else {
                Utils.sortCourseAscending(data.allCourses);
                AllCourses.renderCourses(data.allCourses);
                AllCourses.prepareEnrollment();
                AllCourses.checkEnrollDialogActions();
            }
        },
        error: function (data) {
            console.log(data);
        }
    });
};

AllCourses.renderCourses = function renderCourses(data) {
    var tbodyCourses = $('#tbodyCourses');
    var paginationCourses = $('#paginationCourses');
    var tr;
    for (var i = 0; i < data.length; i++) {
        tr = $('<tr/>');
        tr.append("<td>" + data[i].courseName + "</td>");
        tr.append("<td>" + data[i].category + "</td>");
        tr.append("<td>" + data[i].professor + "</td>");
        tr.append("<td><button class=\"enroll\" value=" + data[i].courseCode + " ><img src=\"/resources/images/rsz_1alta-empresa.png\"></button></td>");
        $(tbodyCourses).append(tr);
    }
    $('#allCourses').show();
    Utils.pagination(tbodyCourses, paginationCourses);
};

AllCourses.prepareEnrollment = function prepareEnrollment() {
    var tbodyCourses = $("#tbodyCourses");
    var enrollButtons = $(tbodyCourses).find("button[class=enroll]");

    for (var i = 0; i < enrollButtons.length; i++) {
        $(enrollButtons[i]).on("click", AllCourses.openEnrollmentDialog);
    }
};

AllCourses.openEnrollmentDialog = function openEnrollmentDialog() {
    $(".cover").fadeIn('slow');
    $("#enrollDialog").fadeIn('slow');
    $("#enroll").attr('data-courseCode', $(this).attr('value'));
};

AllCourses.checkEnrollDialogActions = function checkEnrollDialogActions() {
    $("#enrollDialog").on('click', function () {
        if ($(event.target).is(".close") || $(event.target).is(".cancel")) {
            $(".cover").fadeOut('slow');
            $("#enrollDialog").fadeOut('slow');
        } else if ($(event.target).is("#enroll")) {
            var enrollButton = $("#enroll");
            var courseCode = enrollButton.attr('data-courseCode');
            AllCourses.sendEnrollAjax(courseCode);
        }
    });
};

AllCourses.sendEnrollAjax = function sendEnrollAjax(courseCode) {
    var message = $('#message');
    var jsonParam = JSON.stringify({
        'courseCode': courseCode
    });

    $.ajax({
        url: "/enroll",
        type: 'POST',
        data: jsonParam,
        contentType: 'application/json',
        traditional: true,
        success: function (data) {
            $(".cover").fadeOut('slow');
            $("#enrollDialog").fadeOut('slow');
            if(data.enrollmentResult === "Enrollment successful!") {
                message.text(data.enrollmentResult);
                message.css('font-size', '200%');
                message.css('color', 'green');
                message.fadeIn('slow');
                message.fadeOut('slow');
            } else {
                message.text(data.enrollmentResult);
                message.css('font-size', '200%');
                message.css('color', 'red');
                message.fadeIn('slow');
                message.fadeOut('slow');
            }
        },
        error: function (data) {
            console.log(data);
        }
    });
};

