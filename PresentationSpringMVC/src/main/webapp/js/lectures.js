var Lectures = Lectures || {};

Lectures.displayLecturesProfessor = function displayLecturesProfessor(data, courseName, courseCode) {
    var tbodyLectures = $("#tbodyLectures");
    var paginationLectures = $('#paginationLectures');
    tbodyLectures.empty();
    paginationLectures.empty();
    $('#courseName').text(courseName);
    var tr;
    for (var i = 0; i < data.length; i++) {
        var item = data[i];
        tr = $('<tr/>');
        if (item.attachment === null) {
            tr.append("<td>" + item.name + "</td>");
            tr.append("<td><input value=\"Upload PDF\" data-lectureId="+item.lectureId+" data-courseCode="+courseCode+" name=\"file\" type=\"file\" onchange='Lectures.displayUploadButton(this)'/><button type=\"button\" style=\"display: none\" value=\"Upload\">Upload</button></td>");
            $(tbodyLectures).append(tr);
        } else if (item.attachment !== null) {
            tr.append("<td>" + item.name + "</td>");
            tr.append("<td><a style='float:left' href=\"/download?lectureId="+item.lectureId+"\"><img src=\"/resources/images/rsz_download-pdf.png\"/></a>&nbsp;&nbsp;&nbsp;<button data-lectureId="+item.lectureId+" data-courseCode="+courseCode+" style=\"float:right\" onclick='Lectures.removeAttachment(this)' type=\"button\" value=\"Upload\"><img src=\"/resources/images/rsz_rsz_2delete-2-xxl.png\"/></button></td>");
            tbodyLectures.append(tr);
        }
    }
    $("#courseDetails").show();
    Utils.pagination(tbodyLectures, paginationLectures);
};

Lectures.displayLecturesStudent = function displayLecturesStudent(data, courseName) {
    var tbodyLectures = $("#tbodyLectures");
    var paginationLectures = $('#paginationLectures');
    tbodyLectures.empty();
    paginationLectures.empty();
    $('#courseName').text(courseName);
    var tr;
    for (var i = 0; i < data.length; i++) {
        var item = data[i];
        tr = $('<tr/>');
        if (item.attachment === null) {
            tr.append("<td>" + item.name + "</td>");
            tr.append("<td>No PDF</td>");
            $(tbodyLectures).append(tr);
        } else if (item.attachment !== null) {
            tr.append("<td>" + item.name + "</td>");
            tr.append("<td><a href=\"/download?lectureId="+item.lectureId+"\"><img src=\"/resources/images/rsz_download-pdf.png\"/></a></td>");
            tbodyLectures.append(tr);
        }
    }
    $("#courseDetails").show();
    Utils.pagination(tbodyLectures, paginationLectures);
};

Lectures.displayUploadButton = function displayUploadButton(input) {
    var button = $(input).next();
    $(button).show();
    Lectures.uploadFile(input, button);
};

Lectures.uploadFile = function uploadFile(input, button) {
    $(button).on("click", function() {
        $('#result').html('');
        var lectureId = $(input).attr('data-lectureId');
        var courseCode = $(input).attr('data-courseCode');

        var formData = new FormData($('#fileForm'));
        formData.append('file', input.files[0]);
        formData.append('lectureId', lectureId);
        $.ajax({
            url: "/upload",
            type: 'POST',
            data: formData,
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false
        }).done(function (data) {
            var tbody = $("#tbodyCourses");
            var spanCourseName = tbody.find("span[data-courseCode="+courseCode+"]");
            $(spanCourseName).trigger("click");
        }).fail(function (jqXHR, textStatus) {
            console.log(textStatus);
        });
    })
};

Lectures.removeAttachment = function removeAttachment(removeAtt) {
    var lectureId = $(removeAtt).attr('data-lectureId');
    var courseCode = $(removeAtt).attr('data-courseCode');

    var jsonParam = JSON.stringify({
        'lectureId': lectureId
    });

    $.ajax({
        url: "/removeLectureAttachment",
        type: 'DELETE',
        data: jsonParam,
        contentType: 'application/json',
        traditional: true,
        success: function (data) {
            var tbody = $("#tbodyCourses");
            var spanCourseName = tbody.find("span[data-courseCode="+courseCode+"]");
            $(spanCourseName).trigger("click");
        },
        error: function (data) {
            console.log(data);
        }
    });
};