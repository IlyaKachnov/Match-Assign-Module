var CalendarBasic = function () {
    var initAlert = function () {
        $(document).on('click', '.m_sweetalert', function(e) {
            e.preventDefault();
            var href = $(this).attr("href");

            swal({
                title: 'Отменить?',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Отменить',
                cancelButtonText: "Отмена",
            }).then(function(result) {
                if (result.value) {
                    window.location.href = href;
                }
            });
        });
    };
    return {
        //main function to initiate the module
        init: function () {
            var todayDate = moment().startOf('day');
            var YM = todayDate.format('YYYY-MM');
            var YESTERDAY = todayDate.clone().subtract(1, 'day').format('YYYY-MM-DD');
            var TODAY = todayDate.format('YYYY-MM-DD');
            var TOMORROW = todayDate.clone().add(1, 'day').format('YYYY-MM-DD');
            var eventsList = window.events;
            initAlert();
            $('#m_calendar').fullCalendar({
                locale: 'ru',
                header: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'agendaWeek,listWeek'
                },
                columnFormat: 'ddd D.MM',
                slotLabelFormat: 'H:mm',
                timeFormat: 'H:mm',
                editable: false,
                eventLimit: true, // allow "more" link when too many events
                navLinks: true,
                events: eventsList,

                eventRender: function (event, element) {
                    if (element.hasClass('fc-day-grid-event')) {
                        element.data('content', event.description);
                        element.data('placement', 'top');
//                        mApp.initPopover(element);
                    } else if (element.hasClass('fc-time-grid-event')) {
                        element.find('.fc-title')
                            .append('<div class="fc-description">' + event.description + '</div>');
                        if (event.message !== "") {
                            element.find('.fc-time').append(event.message);
                           mApp.initPopover(element.find("[data-toggle=\"m-popover\"]"));
                        }

                    } else if (element.find('.fc-list-item-title').lenght !== 0) {
                        element.find('.fc-list-item-title').append('<div class="fc-description">' + event.description + '</div>');
                        if (event.message !== "") {
                            $(event.message).insertAfter(element.find('.fc-list-item-title a').first());
                            mApp.initPopover(element.find("[data-toggle=\"m-popover\"]"));
                        }
                    }
                }
            });

        }
    };
}();
var BootstrapSelect = {
    init: function () {
        $(".m_selectpicker").selectpicker()
    }
};
var MessageClass = function () {
    return {
        init: function () {

            var form = $("#message-form");
            var modal = $("#m_modal_4");
            var token = $("meta[name='_csrf']").attr("content");

            var initAjax = function () {
                $.ajaxSetup({
                    headers: {
                        "X-CSRF-TOKEN": token,
                    },
                });
            };

            $(document).on("click", ".add-msg", function () {
                var action = $(this).data("action");
                var slotBody = $(this).closest(".fc-content");
                var slot = slotBody.find(".fc-time");
                var href = $(this).attr("href");
                var listItem = $(".fc-list-item-title a").find('[href="' + href + '"]').closest(".fc-list-item-title");
                // var form = $("#message-form");
                // var modal = $("#m_modal_4");
                // var token = $("meta[name='_csrf']").attr("content");

                $(document).on('click', '#submit-form', function () {
                    var text = $("#message-text").val();
                    initAjax();
                    $.ajax({
                        type: "POST",
                        data: form.serialize(),
                        url: action,
                        success: function () {
                            modal.modal("hide");
                            setTimeout(function () {
                                location.reload();
                            }, 100);
                            // slot.append('<span tabindex="0" role="button" class="m-badge m-badge--danger" ' +
                            //     'data-toggle="m-popover" data-trigger="focus" data-content="' + text + '" data-original-title="Сообщение">!</span>');
                            // $("a.add-msg", slotBody).remove();
                            // listItem.append('<span tabindex="0" role="button" class="m-badge m-badge--danger" ' +
                            //     'data-toggle="m-popover" data-trigger="focus" data-content="' + text + '" data-original-title="Сообщение">!</span>');
                            // listItem.find(".add-msg").remove();
                            // slot.find(".m-badge").popover();
                        }
                    });
                });


            });

            $(document).on("click", ".delete-msg", function (e) {
                var deleteHref = $(this).data("href");
                // var slotBody = $(this).closest(".fc-content");
                // var listItem = $(".fc-list-item-title a").find('[href="' + deleteHref + '"]').closest(".fc-list-item-title");
                e.preventDefault();
                swal({
                    title: 'Удалить?',
                    type: 'warning',
                    showCancelButton: true,
                    confirmButtonText: 'Удалить',
                    cancelButtonText: "Отмена",
                }).then(function(result) {
                    if (result.value) {
                        initAjax();
                        console.log(deleteHref);
                        $.ajax({
                            type:"POST",
                            url:deleteHref,
                            success: function () {
                                // $(".m-badge", slotBody).remove();
                                // $(".m-badge", listItem).remove();
                                setTimeout(function () {
                                    location.reload();
                                }, 100);
                            }
                        })
                    }
                });

            });

        }

    }
}();
jQuery(document).ready(function () {
    MessageClass.init();
    CalendarBasic.init();
    BootstrapSelect.init();
});