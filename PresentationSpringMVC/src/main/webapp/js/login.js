$(document).ready(function () {
    checkFields();
    login();
});

function checkFields() {
    // Email must be valid
    $('#login_email').on('input', function () {
        var input = $(this);
        var re = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
        var is_email = re.test(input.val());
        if (is_email) {
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

    // Password can't be blank
    $('#login_password').on('input', function () {
        var input = $(this);
        var is_name = input.val();
        if (is_name) {
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
}

function login() {
    $('#submit').click(function (event) {
        var form_data = $("#login input[type=text], input[type=password]").serializeArray();
        var error_free = true;
        for (var input in form_data) {
            var element = $("#login_" + form_data[input]['name']);
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
            var email = $('#login_email').val();
            var password = $('#login_password').val();

            var jsonParams = JSON.stringify({
                'email': email,
                'password': password
            });

            $.ajax({
                type: 'POST',
                contentType: 'application/json',
                dataType: 'json',
                url: 'doLogin',
                data: jsonParams,
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("Accept", "application/json");
                    xhr.setRequestHeader("Content-Type", "application/json");
                },
                success: function (data) {
                    var message = $('#message');
                    switch(data.loginResult) {
                        case "AllCourses":
                            window.location.href = "/WEB-INF/views/allCourses.jsp";
                            break;
                        case "ProfessorCourses":
                            window.location.href = "/WEB-INF/views/professorCourses.jsp";
                            break;
                        case "New Account":
                            window.location.href = "/WEB-INF/views/addNewAccount.jsp";
                            break;
                        case "Wrong password!":
                            message.text("Wrong password!");
                            message.css('color', 'red');
                            message.show('slow');
                            break;
                        default:
                            message.text("That user does not exists!");
                            message.css('color', 'red');
                            message.show('slow');
                    }
                },
                error: function (data) {
                    console.log(data);
                }
            });

         /*   $.ajax({
                type: 'POST',
                contentType: 'application/json',
                url: 'dologin?email='+email+'&password='+password,
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("Accept", "application/json");
                    xhr.setRequestHeader("Content-Type", "application/json");
                },
                success: function (data) {
                   console.log(data);
                },
                error: function (data) {
                    console.log(data);
                }
            });*/
        }
    });
}

