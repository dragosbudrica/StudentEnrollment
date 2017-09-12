var StudentCourses = StudentCourses || {};

$(document).ready(function () {
    StudentCourses.getStudentCourses();
});

StudentCourses.getStudentCourses = function getStudentCourses() {
    var warning = $("#warning");
    $.ajax({
        url: '/getStudentCourses',
        type: 'GET',
        dataType: 'json',
        traditional: true,
        success: function (data) {
            if (data.length === 0) {
                $(warning).find('h1').text("You aren't enrolled to any course yet!");
                warning.show();
            } else {
                Utils.sortCourseAscending(data);
                StudentCourses.renderCourses(data);
                Utils.prepareDisplayCourseDetails();
            }
        },
        error: function (data) {
            console.log(data);
        }
    });
};

StudentCourses.renderCourses = function renderCourses(data) {
    var tbodyCourses = $('#tbodyCourses');
    var paginationCourses = $('#paginationCourses');
    var tr;
    for (var i = 0; i < data.length; i++) {
        tr = $('<tr/>');
        tr.append("<td><span data-courseCode=" + data[i].courseCode + " class=\"courseNameClass\">" + data[i].courseName + "</span></td>");
        tr.append("<td>" + data[i].category + "</td>");
        tr.append("<td>" + data[i].professor + "</td>");
        tbodyCourses.append(tr);
    }
    $('#studentCourses').show();
    Utils.pagination(tbodyCourses, paginationCourses);
};




