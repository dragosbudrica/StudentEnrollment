var Validation = Validation || {};

$(document).ready(function () {
    Validation.getAllCourses();
});

Validation.getAllCourses = function getAllCourses() {
    var warning = $("#warning");
    $.ajax({
        url: '/getAllCourses',
        type: 'GET',
        dataType: 'json',
        traditional: true,
        success: function (data) {
            if (data.length === 0) {
                $(warning).find('h1').text("There are no courses added yet!");
                warning.show();
            } else {
                Utils.sortCourseAscending(data);
                Validation.renderCourses(data);
                Validation.prepareDisplayEnrolledStudents();
                Validation.checkValidateAction();
                Validation.checkInvalidateAction();
            }
        },
        error: function (data) {
            console.log(data);
        }
    });
};

Validation.renderCourses = function renderCourses(data) {
    var tbodyCourses = $('#tbodyCourses');
    var paginationCourses = $('#paginationCourses');
    var tr;
    for (var i = 0; i < data.length; i++) {
        tr = $('<tr/>');
        tr.append("<td><span data-courseCode=" + data[i].courseCode + " class=\"courseNameClass\">" + data[i].courseName + "</span></td>");
        tr.append("<td>" + data[i].category + "</td>");
        tr.append("<td>" + data[i].professor + "</td>");
        $(tbodyCourses).append(tr);
    }
    $('#allCourses').show();
    Utils.pagination(tbodyCourses, paginationCourses);
};

Validation.prepareDisplayEnrolledStudents = function prepareDisplayEnrolledStudents() {
    var spansCourseName = $('#grid').find("span[class=courseNameClass]");

    for (var i = 0; i < spansCourseName.length; i++) {
        $(spansCourseName[i]).on("click", Validation.getEnrolledStudents);
    }
};

Validation.getEnrolledStudents = function getEnrolledStudents() {
    var warning2 = $("#warning2");
    var validation = $('#validation');
    $('#tbodyValidation').empty();
    $('#paginationValidation').empty();
    $('#options').val("Choose An Option");
    Validation.disableValidationButtons();

    var courseCode = $(this).attr('data-courseCode');
    $('#courseTitle').text($(this).text());
    validation.attr('data-courseCode', courseCode);

    var jsonParam = JSON.stringify({
        'courseCode': courseCode
    });

    $.ajax({
        url: "/getEnrolledStudents",
        type: 'POST',
        data: jsonParam,
        contentType: "application/json",
        success: function (data) {
            if (data.length === 0) {
                validation.hide();
                $(warning2).find('h1').text("There are no enrolled students at this course yet!");
                warning2.show();
            } else {
                warning2.hide();
                Utils.sortStudentsAscending(data);
                Validation.renderStudents(data);
            }
        },
        error: function (data) {
            console.log(data);
        }
    });
};

Validation.renderStudents = function renderStudents(data) {
    var warning2 = $("#warning2");
    var validation = $('#validation');

    var tbodyValidation = $('#tbodyValidation');
    var paginationValidation = $('#paginationValidation');
    var tr;
    var result;
    var validated;
    var index;
    var isEverythingEvaluated = true;
    for(index = 0; index < data.length; index++) {
        if(data[index].result === null) {
            isEverythingEvaluated = false;
            break;
        }
    }

    if(isEverythingEvaluated) {
        warning2.hide();
        for (index = 0; index < data.length; index++) {
            tr = $('<tr/>');
            tr.attr('data-userId', data[index].userId);
            result = data[index].result;
            validated = data[index].validated;

            tr.attr('data-result', result);
            tr.append("<td><input type=\"checkbox\"></td>");
            tr.append("<td>" + data[index].lastName + "</td>");
            tr.append("<td>" + data[index].firstName + "</td>");
            tr.append("<td>" + result + "</td>");
            if (validated) {
                tr.append("<td><img src=\"/resources/images/rsz_agt_action_success_256.png\"/></td>");
                tr.attr('class', 'valid');
            } else {
                tr.append("<td><img src=\"/resources/images/rsz_s43qy.png\"/></td>");
                tr.attr('class', 'invalid');
            }
            tbodyValidation.append(tr);

        }
        validation.show();
        Utils.pagination(tbodyValidation, paginationValidation);
        Validation.prepareManualCheckUncheck();
    } else {
        validation.hide();
        $(warning2).find('h1').text("The professor did not evaluated all the results yet!");
        warning2.show();
    }
};

Validation.executeAction = function executeAction() {
    var tbodyValidation = $('#tbodyValidation');
    var option = $('#options').val();
    var allCheckBoxes = tbodyValidation.find("input[type=checkbox]");

    if (option !== "Choose An Option") {
        switch (option) {
            case "Select All":
                Validation.selectAllCheckBoxes(allCheckBoxes);
                break;
            case "Deselect All":
                Validation.deselectAllCheckBoxes(allCheckBoxes);
                break;
            case "Select Only Absents":
                Validation.selectOnlyAbsentCheckBoxes(allCheckBoxes);
                break;
            case "Select Only Grades":
                Validation.selectOnlyGradesCheckBoxes(allCheckBoxes);
                break;
            case "Select Validated Results":
                Validation.selectOnlyValidatedCheckBoxes(allCheckBoxes);
                break;
            default:
                Validation.selectOnlyInvalidatedCheckBoxes(allCheckBoxes);
                break;
        }
    }
};

Validation.selectAllCheckBoxes = function selectAllCheckBoxes(allCheckBoxes) {
    var validated, numberOfValidated = 0, numberOfInvalidated = 0;
    for (var index = 0; index < allCheckBoxes.length; index++) {
        allCheckBoxes[index].checked = true;
        validated = $(allCheckBoxes[index]).parent().parent().attr('class');
        if(validated === 'valid') {
            numberOfValidated++;
        } else {
            numberOfInvalidated++;
        }
    }

    if(numberOfValidated > 0 && numberOfInvalidated > 0) {
            Validation.enableValidationButtons();
    } else if(numberOfInvalidated === 0) {
            Validation.disableValidateEnableInvalidate();
    } else {
        Validation.enableValidateDisableInvalidate();
    }
};

Validation.deselectAllCheckBoxes = function deselectAllCheckBoxes(allCheckBoxes) {
    for (var index = 0; index < allCheckBoxes.length; index++) {
        allCheckBoxes[index].checked = false;
    }
    Validation.disableValidationButtons();
};

Validation.selectOnlyAbsentCheckBoxes = function selectOnlyAbsentCheckBoxes(allCheckBoxes) {
    var validated, numberOfValidated = 0, numberOfInvalidated = 0;
    var result;
    var isSomethingChecked;

    for (var index = 0; index < allCheckBoxes.length; index++) {
        result = $(allCheckBoxes[index]).parent().parent().attr('data-result');
        if (result === 'Absent') {
            allCheckBoxes[index].checked = true;
            validated = $(allCheckBoxes[index]).parent().parent().attr('class');
            if(validated === 'valid') {
                numberOfValidated++;
            } else {
                numberOfInvalidated++;
            }
            isSomethingChecked = true;
        } else {
            allCheckBoxes[index].checked = false;
        }
    }
    if(isSomethingChecked && numberOfValidated > 0 && numberOfInvalidated > 0) {
        Validation.enableValidationButtons();
    } else if(isSomethingChecked && numberOfInvalidated === 0) {
        Validation.disableValidateEnableInvalidate();
    } else if(isSomethingChecked && numberOfValidated === 0) {
        Validation.enableValidateDisableInvalidate();
    } else {
        Validation.disableValidationButtons();
    }
};

Validation.selectOnlyGradesCheckBoxes = function selectOnlyGradesCheckBoxes(allCheckBoxes) {
    var validateButton = $('#validateBtn');
    var invalidateButton = $('#invalidateBtn');
    var validated, numberOfValidated = 0, numberOfInvalidated = 0;

    var result;
    var isSomethingChecked;

    for (var index = 0; index < allCheckBoxes.length; index++) {
        result = $(allCheckBoxes[index]).parent().parent().attr('data-result');
        if (result !== 'Absent') {
            allCheckBoxes[index].checked = true;
            validated = $(allCheckBoxes[index]).parent().parent().attr('class');
            if(validated === 'valid') {
                numberOfValidated++;
            } else {
                numberOfInvalidated++;
            }
            isSomethingChecked = true;
        } else {
            allCheckBoxes[index].checked = false;
        }
    }
    if(isSomethingChecked && numberOfValidated > 0 && numberOfInvalidated > 0) {
        Validation.enableValidationButtons();
    } else if(isSomethingChecked && numberOfInvalidated === 0) {
        Validation.disableValidateEnableInvalidate();
    } else if(isSomethingChecked && numberOfValidated === 0) {
        Validation.enableValidateDisableInvalidate();
    } else {
        Validation.disableValidationButtons();
    }
};

Validation.selectOnlyValidatedCheckBoxes = function selectOnlyValidatedCheckBoxes(allCheckBoxes) {
    var validated;
    var isSomethingChecked;

    for (var index = 0; index < allCheckBoxes.length; index++) {
        validated = $(allCheckBoxes[index]).parent().parent().attr('class');
        if (validated === "valid") {
            allCheckBoxes[index].checked = true;
            isSomethingChecked = true;
        } else {
            allCheckBoxes[index].checked = false;
        }
    }
    if(isSomethingChecked) {
        Validation.disableValidateEnableInvalidate();
    } else {
        Validation.disableValidationButtons();
    }
};

Validation.selectOnlyInvalidatedCheckBoxes = function selectOnlyInvalidatedCheckBoxes(allCheckBoxes) {
    var validated;
    var isSomethingChecked;

    for (var index = 0; index < allCheckBoxes.length; index++) {
        validated = $(allCheckBoxes[index]).parent().parent().attr('class');
        if (validated === "invalid") {
            allCheckBoxes[index].checked = true;
            isSomethingChecked = true;
        } else {
            allCheckBoxes[index].checked = false;
        }
    }
    if(isSomethingChecked) {
        Validation.enableValidateDisableInvalidate();
    } else {
        Validation.disableValidationButtons();
    }
};

Validation.checkValidateAction = function checkValidateAction() {
    $('#validateBtn').click(Validation.validateSelectedResults);
};

Validation.checkInvalidateAction = function checkInvalidateAction() {
    $('#invalidateBtn').click(Validation.invalidateSelectedResults);
};

Validation.validateSelectedResults = function validateSelectedResults() {
    var courseCode = $('#validation').attr('data-courseCode');
    var jsonParams = Validation.prepareParams(courseCode);

    $.ajax({
        url: '/validate',
        type: 'PUT',
        data: jsonParams,
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

Validation.invalidateSelectedResults = function invalidateSelectedResults() {
    var courseCode = $('#validation').attr('data-courseCode');
    var jsonParams = Validation.prepareParams(courseCode);
    $.ajax({
        url: '/invalidate',
        type: 'PUT',
        data: jsonParams,
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

Validation.prepareParams = function prepareParams(courseCode) {
    var userIds = [];
    var checkedTrs = $('#tbodyValidation').find("tr:has(input:checked)");
    for(var index = 0; index < checkedTrs.length; index++) {
        var userId = $(checkedTrs[index]).attr('data-userId');
        userIds.push(userId);
    }

    return JSON.stringify({
        'courseCode': courseCode,
        'userIds': userIds
    });
};

Validation.enableValidationButtons = function enableValidationButtons() {
    var validateButton = $('#validateBtn');
    var invalidateButton = $('#invalidateBtn');

    validateButton.attr('disabled', false);
    invalidateButton.attr('disabled', false);

    validateButton.attr('title', "You can validate all the selected entries!");
    invalidateButton.attr('title', "You can invalidate all the selected entries!");
};

Validation.disableValidationButtons = function disableValidationButtons() {
    var validateButton = $('#validateBtn');
    var invalidateButton = $('#invalidateBtn');

    validateButton.attr('disabled', true);
    invalidateButton.attr('disabled', true);

    validateButton.attr('title', "There's nothing to validate!");
    invalidateButton.attr('title', "There's nothing to invalidate!");
};

Validation.disableValidateEnableInvalidate = function disableValidateEnableInvalidate() {
    var validateButton = $('#validateBtn');
    var invalidateButton = $('#invalidateBtn');

    $(validateButton).attr('disabled', true);
    $(invalidateButton).attr('disabled', false);

    $(validateButton).attr('title', "There's nothing to validate!");
    $(invalidateButton).attr('title', "You can invalidate all the selected entries!");
};

Validation.enableValidateDisableInvalidate = function enableValidateDisableInvalidate() {
    var validateButton = $('#validateBtn');
    var invalidateButton = $('#invalidateBtn');

    $(validateButton).attr('disabled', false);
    $(invalidateButton).attr('disabled', true);

    $(validateButton).attr('title', "You can validate all the selected entries!");
    $(invalidateButton).attr('title', "There's nothing to invalidate!");
};

Validation.prepareManualCheckUncheck = function prepareManualCheckUncheck() {
    $(":checkBox").change(Validation.manualCheckUncheck);
};

Validation.manualCheckUncheck = function manualCheckUncheck() {
    var allCheckBoxes = $('#tbodyValidation').find("input[type=checkbox]");

    var validated, numberOfValidated = 0, numberOfInvalidated = 0;
    for (var index = 0; index < allCheckBoxes.length; index++) {
        var currentCheckBox = allCheckBoxes[index];
        validated = $(currentCheckBox).parent().parent().attr('class');
        if($(currentCheckBox).is(':checked')) {
            if(validated === 'valid') {
                numberOfValidated++;
            } else {
                numberOfInvalidated++;
            }
        }
        if(numberOfValidated > 0 && numberOfInvalidated > 0) {
            Validation.enableValidationButtons();
            break;
        }
    }

    if(numberOfValidated > 0 && numberOfInvalidated === 0) {
        Validation.disableValidateEnableInvalidate();
    } else if(numberOfValidated === 0 && numberOfInvalidated > 0) {
        Validation.enableValidateDisableInvalidate();
    } else if(numberOfValidated === 0 && numberOfInvalidated === 0) {
        Validation.disableValidationButtons();
    }
};











