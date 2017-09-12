var NewCourse = NewCourse || {};

$(document).ready(function () {
    NewCourse.getCategories();
});

NewCourse.getCategories = function getCategories() {
    $.ajax({
        url: '/getAllCategories',
        type: 'GET',
        dataType: 'json',
        traditional: true,
        success: function (data) {
            console.log(data);
            for (index = 0; index < data.allCategories.length; ++index) {
                var category = "<option value=" + data.allCategories[index].categoryName + ">" + data.allCategories[index].categoryName + "</option>";
                $(category).appendTo('#categoryAddCourse');
            }
            NewCourse.checkFields();
            NewCourse.prepareCourseCreation();
        },
        error: function (data) {
            console.log(data);
        }
    });
};

NewCourse.checkFields = function checkFields() {
    // Course Name can't be blank
    $('#newCourse_courseName').on('input', function () {
        var input = $(this);
        var is_courseName = input.val();
        if (is_courseName) {
            input.removeClass("invalid").addClass("valid");
            if (input.next().hasClass("error_show")) {
                input.next().removeClass("error_show").addClass("error");
            }
        }
        else {
            input.removeClass("valid").addClass("invalid");
            input.next().removeClass("error").addClass("error_show");
        }
    });

    // Description must be valid
    $('#newCourse_description').on('input', function () {
        var input = $(this);
        var is_description = input.val();
        if (is_description) {
            input.removeClass("invalid").addClass("valid");
            if (input.next().hasClass("error_show")) {
                input.next().removeClass("error_show").addClass("error");
            }
        }
        else {
            input.removeClass("valid").addClass("invalid");
            input.next().removeClass("error").addClass("error_show");
        }
    });
};

NewCourse.prepareCourseCreation = function prepareCourseCreation() {
    $('#submit').click(function (event) {
        var form_data = $("#newCourse input[type=text], textarea").serializeArray();
        var error_free = true;
        for (var input in form_data) {
            var element = $("#newCourse_" + form_data[input]['name']);
            var valid = element.hasClass("valid");
            var error_element = $("span", element.parent());
            if (!valid) {
                error_element.removeClass("error").addClass("error_show");
                error_free = false;
            }
            else {
                error_element.removeClass("error_show").addClass("error");
            }
        }
        if (!error_free) {
            event.preventDefault();
        }
        else {
            var courseName = $('#newCourse_courseName').val();
            var category = $('#categoryAddCourse').val();
            var numberOfLectures = $('#numberOfLectures').val();
            var description = $('#newCourse_description').val();

            NewCourse.sendCreateCourseAjax(courseName, category, numberOfLectures, description);
        }
    });
};

NewCourse.sendCreateCourseAjax = function sendCreateCourseAjax(courseName, category, numberOfLectures, description) {
    var jsonParams = JSON.stringify({
        'courseName': courseName,
        'category': category,
        'numberOfLectures': numberOfLectures,
        'description': description
    });

    $.ajax({
        url: '/addNewCourse',
        type: 'POST',
        data: jsonParams,
        contentType: 'application/json',
        traditional: true,
        success: function (data) {
            var message = $('#message');
            if (data.newCourseResult === "Course creation successful!") {
                message.text("Course Creation Successful!");
                message.css('color', 'green');
                message.fadeIn('slow');
                message.fadeOut('slow');
            }
            else {
                message.text(data.newCourseResult);
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
