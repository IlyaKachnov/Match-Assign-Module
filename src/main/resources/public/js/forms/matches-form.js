//== Class definition

var FormWidgets = function () {
    //== Private functions
    var validator;
    // if(teamList != null){
    //     console.log(JSON.stringify(teamList));
    // }
    var initWidgets = function () {
        $.fn.datepicker.dates['ru'] = {
            days: ["Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"],
            daysShort: ["Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"],
            daysMin: ["Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"],
            months: ["Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"],
            monthsShort: ["Янв", "Фев", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен", "Окт", "Нов", "Дек"],
            today: "Сегодня",
            clear: "Очистить",
            weekStart: 1
        };

        // select2
        $('#homeTeam').select2({
            // placeholder: "Выберите хозяев",
        });
        $('#homeTeam').on('change', function () {
            validator.element($(this)); // validate element
        });
        $('#guestTeam').select2({
            // placeholder: "Выберите гостей",
        });
        $('#guestTeam').on('change', function () {
            validator.element($(this)); // validate element
        });

        $("#m_select2_2").select2({
            placeholder: "Выберите тур",
            allowClear: true
        });
        $("#m_select2_2").on("change", function () {
            var id = $(this).find("option:selected").parent().data("id");
            var token = $("meta[name='_csrf']").attr("content");
            var homeList = $("#homeTeam");
            var guestList = $("#guestTeam");
            $.ajaxSetup({
                headers: {
                    "X-CSRF-TOKEN": token,
                },
            });
            $.ajax({
                url: "/matches/" + id + "/change",
                type: "GET",
                success: function (response) {
                    var data = JSON.parse(response);
                    homeList.empty().select2({
                        data: $.map(data, function (item) {
                            return {
                                id: item.id,
                                text: item.name,
                            }
                        })
                    });
                    guestList.empty().select2({
                        data: $.map(data, function (item) {
                            return {
                                id: item.id,
                                text: item.name,
                            }
                        })
                    });
                }
            });
        });

        $('#m_select2_2').on('change', function () {
            validator.element($(this)); // validate element
        });

    };

    var initValidation = function () {
        validator = $("#m_form_1").validate({
            // define validation rules
            rules: {
                // matchDate: {
                //     required: true,
                //     date: true
                // },
                homeTeam: {
                    required: true
                },
                guestTeam: {
                    required: true
                },
                tour: {
                    required: true
                }
            },
            messages: {},

            //display error alert on form submit
            invalidHandler: function (event, validator) {
                var alert = $('#m_form_1_msg');
                alert.removeClass('m--hide').show();
                mApp.scrollTo(alert, -200);
            },

            submitHandler: function (form) {
                form[0].submit(); // submit the form
            }
        });
    }

    return {
        // public functions
        init: function () {
            initWidgets();
            initValidation();
        }
    };
}();

jQuery(document).ready(function () {
    FormWidgets.init();
});