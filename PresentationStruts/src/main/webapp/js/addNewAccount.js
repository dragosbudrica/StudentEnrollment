var NewAccount = NewAccount || {};

$(document).ready(function () {
    NewAccount.checkFields();
    NewAccount.prepareAccountCreation();
});

NewAccount.checkFields = function checkFields() {
    // First Name can't be blank
    $('#newAccount_firstName').on('input', function () {
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

    // Last Name can't be blank
    $('#newAccount_lastName').on('input', function () {
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

    // SSN must be valid
    $('#newAccount_ssn').on('input', function () {
        var input = $(this);
        var isSsn = NewAccount.checkSsn(input.val());
        if (isSsn) {
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

    // Email must be valid
    $('#newAccount_email').on('input', function () {
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
    $('#newAccount_password').on('input', function () {
        var input = $(this);
        var is_password = input.val();
        if (is_password) {
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

NewAccount.checkSsn = function checkSsn(cnp) {
    if (cnp.length < 13 || cnp.length > 13 || isNaN(cnp))
        return false;
    else {
        var sum = parseInt(cnp[0]) * 2 +
            parseInt(cnp[1]) * 7 +
            parseInt(cnp[2]) * 9 +
            parseInt(cnp[3]) +
            parseInt(cnp[4]) * 4 +
            parseInt(cnp[5]) * 6 +
            parseInt(cnp[6]) * 3 +
            parseInt(cnp[7]) * 5 +
            parseInt(cnp[8]) * 8 +
            parseInt(cnp[9]) * 2 +
            parseInt(cnp[10]) * 7 +
            parseInt(cnp[11]) * 9;

        var modulus = sum % 11;

        var valid = false;
        if ((modulus < 10 && modulus.toString() === cnp[12]) || (modulus === 10 && cnp[12].toString() === '1')) {
            valid = true;
        }

        return valid;
    }
};

NewAccount.prepareAccountCreation = function prepareAccountCreation() {
    $('#submit').click(function (event) {
            var form_data = $("#newAccount input[type=text], input[type=password]").serializeArray();
            var error_free = true;
            for (var input in form_data) {
                var element = $("#newAccount_" + form_data[input]['name']);
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
                var firstName = $('#newAccount_firstName').val();
                var lastName = $('#newAccount_lastName').val();
                var ssn = $('#newAccount_ssn').val();
                var email = $('#newAccount_email').val();
                var password = $('#newAccount_password').val();
                var role = $('#role').val();

                NewAccount.sendCreateAccountAjax(firstName, lastName, ssn, email, password, role);
            }
        });
};

NewAccount.sendCreateAccountAjax = function sendCreateAccountAjax(firstName, lastName, ssn, email, password, role) {
    var jsonParams = JSON.stringify({
        'firstName': firstName,
        'lastName': lastName,
        'ssn': ssn,
        'email': email,
        'password': password,
        'role': role
    });

    $.ajax({
        url: '/addNewAccount',
        type: 'POST',
        data: jsonParams,
        contentType: 'application/json',
        success: function (data) {
            var message = $('#message');
            console.log(data);
            if (data.newAccountResult === "Account creation successful!") {
                message.text(data.newAccountResult);
                message.css('color', 'green');
                message.fadeIn('slow');
                message.fadeOut('slow');
            }
            else {
                message.text(data.newAccountResult);
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
