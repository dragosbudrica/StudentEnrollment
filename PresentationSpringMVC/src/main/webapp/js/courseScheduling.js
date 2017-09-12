var CourseScheduling = CourseScheduling || {};
CourseScheduling.events = [];

$(document).ready(function () {
    CourseScheduling.getEvents();
    CourseScheduling.checkEventContentDialogActions();
    CourseScheduling.checkConfirmationDialogActions();
});

CourseScheduling.getEvents = function getEvents() {
    $.ajax({
        url: '/getCourseEvents',
        type: 'GET',
        dataType: 'json',
        traditional: true,
        success: function (data) {
            CourseScheduling.events = CourseScheduling.buildEvents(data);
            CourseScheduling.displayEvents(CourseScheduling.events);
        },
        error: function (data) {
            console.log(data);
        }
    });
};

CourseScheduling.buildEvents = function buildEvents(dataEvents) {
    var events = [];
    for (index = 0; index < dataEvents.length; ++index) {
        var startDate = new Date(dataEvents[index].startTime);
        var endDate = new Date(dataEvents[index].startTime);
        events.push({
            'title': dataEvents[index].courseName,
            'start': startDate,
            'end': endDate
        });
    }
    return events;
};

CourseScheduling.getCourseTitles = function getCourseTitles() {
    $.ajax({
        url: '/getCourseTitles',
        type: 'GET',
        dataType: 'json',
        traditional: true,
        success: function (data) {
            console.log(data);
            for (index = 0; index < data.length; ++index) {
                var div_data = "<option value=" + data[index] + ">" + data[index] + "</option>";
                $(div_data).appendTo('#courseName');
            }
        },
        error: function (data) {
            console.log(data);
        }
    });
};


CourseScheduling.displayEvents = function displayEvents(events) {
    $('#calendar').fullCalendar({
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,agendaWeek,agendaDay,listWeek'
        },
        navLinks: true, // can click day/week names to navigate views
        editable: false,
        eventLimit: true, // allow "more" link when too many events
        events: events,
        eventClick: function (event, element) {
            $("#startTime").val(event.start._i);
            CourseScheduling.getCourseTitles();
            $(".cover").fadeIn('slow');
            $("#eventContent").fadeIn('slow');
        },
        dayClick: function (date, jsEvent, view) {
            var formattedDate = CourseScheduling.formatDateTime(date);
            $("#startTime").val(formattedDate);
            CourseScheduling.getCourseTitles();
            $(".cover").fadeIn('slow');
            $("#eventContent").fadeIn('slow');
        }
    });
};

CourseScheduling.checkEventContentDialogActions = function checkEventContentDialogActions() {
    $("#eventContent").on('click', function () {
        if ($(event.target).is(".close")) {
            $(".cover").fadeOut('slow');
            $("#eventContent").fadeOut('slow');
            $('#courseName').empty();
        } else if($(event.target).is("#submit")) {
            $("#eventContent").fadeOut('slow');
            $("#confirmDialog").fadeIn('slow');
        }
    });
};

CourseScheduling.checkConfirmationDialogActions = function checkConfirmationDialogActions() {
    $("#confirmDialog").on('click', function () {
        if ($(event.target).is(".close") || $(event.target).is(".cancel")) {
            $(".cover").fadeOut('slow');
            $("#confirmDialog").fadeOut('slow');
            $('#courseName').empty();
        } else if($(event.target).is("#schedule")) {
            CourseScheduling.schedule();
        }
    });
};


CourseScheduling.formatDateTime = function formatDateTime(date) {
    var month, day, hours, minutes, seconds;
    if (date._d.getMonth() < 9)
        month = "0" + (date._d.getMonth() + 1);
    else
        month = date._d.getMonth() + 1;

    if (date._d.getDate() < 10)
        day = "0" + date._d.getDate();
    else
        day = date._d.getDate();

    if (date._d.getHours() < 10)
        hours = "0" + date._d.getHours();

    if (date._d.getMinutes() < 10)
        minutes = "0" + date._d.getMinutes();

    if (date._d.getSeconds() < 10)
        seconds = "0" + date._d.getSeconds();

    return date._d.getFullYear() + "-" + month + "-" + day + "T" + hours + ":" + minutes + ":" + seconds;
};

CourseScheduling.schedule = function schedule() {
    var jsonParam = JSON.stringify({
        'courseName': $('#courseName').find('option:selected').text(),
        'startTime': $('#startTime').val()
    });

    var message = $('#message');
    var calendar = $('#calendar');
    $.ajax({
        url: '/schedule',
        type: 'PUT',
        data: jsonParam,
        dataType: "json",
        contentType: 'application/json',
        success: function (data) {
            console.log(data);
            if (data.length !== 0) {
                message.text("Scheduling successful!");
                message.css('font-size', '200%');
                message.css('color', 'green');
                message.fadeIn('slow');
                message.fadeOut('slow');
                CourseScheduling.events = CourseScheduling.buildEvents(data);
                calendar.fullCalendar('removeEvents');
                calendar.fullCalendar('addEventSource', CourseScheduling.events);
                calendar.fullCalendar('rerenderEvents' );
            }
            else {
                message.text("Invalid Date!");
                message.css('font-size', '200%');
                message.css('color', 'red');
                message.fadeIn('slow');
                message.fadeOut('slow');
            }
            $('.cover').fadeOut('slow');
            $("#confirmDialog").fadeOut('slow');
            $('#courseName').empty();
        },
        error: function (data) {
            console.log(data);
            $('.cover').fadeOut('slow');
            $("#confirmDialog").fadeOut('slow');
            $('#courseName').empty();
        }
    });
};




