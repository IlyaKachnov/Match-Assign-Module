//== Class definition

var FormWidgets = function () {
    //== Private functions
    var validator;

    var initWidgets = function() {
        // datepicker

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
        $('#startDate').datepicker({
            language: 'ru',
            todayHighlight: true,
            format: 'yyyy-mm-dd',
            templates: {
                leftArrow: '<i class="la la-angle-left"></i>',
                rightArrow: '<i class="la la-angle-right"></i>'
            }
        });
        $('#endDate').datepicker({
            language: 'ru',
            todayHighlight: true,
            format: 'yyyy-mm-dd',
            templates: {
                leftArrow: '<i class="la la-angle-left"></i>',
                rightArrow: '<i class="la la-angle-right"></i>'
            }
        });

        // timepicker
        $('#startTime').timepicker({
            minuteStep: 10,
            showSeconds: false,
            showMeridian: false
        });

        $('#endTime').timepicker({
            minuteStep: 10,
            showSeconds: false,
            showMeridian: false
        });

        // select2
        $('#league').select2({
            placeholder: "Выберите лигу",
        });
        $('#league').on('select2:change', function(){
            validator.element($(this)); // validate element
        });

    }

    var initValidation = function () {
        validator = $( "#m_form_1" ).validate({
            // define validation rules
            rules: {
                league: {
                    required:true
                },
                endDate: {
                    required: true,
                    date: true
                },
                startDate: {
                    required: true,
                    date: true
                },

                startTime: {
                    required: true
                },
                endTime: {
                    required: true
                },

            },
            messages: {
              league: {
                  required: "Поле обязательно для заполнения",
              },
                endDate: {
                    required: "Поле обязательно для заполнения",
                    date: "Неверный формат даты",
                },
                startDate: {
                    required: "Поле обязательно для заполнения",
                    date: "Неверный формат даты",
                },
                startTime: {
                    required: "Поле обязательно для заполнения",
                    date: "Неверный формат даты",
                },
                endTime: {
                    required: "Поле обязательно для заполнения",
                    date: "Неверный формат даты",
                },
            },

            //display error alert on form submit
            invalidHandler: function(event, validator) {
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
        init: function() {
            initWidgets();
            initValidation();
        }
    };
}();

jQuery(document).ready(function() {
    FormWidgets.init();
});