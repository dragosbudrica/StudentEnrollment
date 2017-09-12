var Timetable = Timetable || {};

$(document).ready(function () {
    Timetable.getEvents();
});

Timetable.getEvents = function getEvents() {
    var index;
    var courses = [];
    $.ajax({
        url: '/getEvents',
        type: 'GET',
        dataType: 'json',
        traditional: true,
        success: function (data) {
            if(data.events.length > 0) {
                for(index = 0; index < data.events.length; ++index) {
                    courses.push({'title' : data.events[index].courseName, 'start' : new Date(data.events[index].startTime), 'end': new Date(data.events[index].endTime)});
                }
                Timetable.displayEvents(courses);
            } else {
                $("#calendar").hide();
                $("#image").show();
            }
        },
        error: function (data) {
            console.log(data);
        }
    });
};

Timetable.displayEvents = function displayEvents(courses) {
    $('#calendar').fullCalendar({
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,agendaWeek,agendaDay,listWeek'
        },
        navLinks: true, // can click day/week names to navigate views
        editable: false,
        eventLimit: true, // allow "more" link when too many events
        events:  courses
    });
};
