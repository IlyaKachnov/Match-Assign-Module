//== Class definition

var FormWidgets = function () {
    //== Private functions
    var validator;

    var initWidgets = function() {
        // datepicker
        $('#startDate').datepicker({
            todayHighlight: true,
            format: 'yyyy-mm-dd',
            templates: {
                leftArrow: '<i class="la la-angle-left"></i>',
                rightArrow: '<i class="la la-angle-right"></i>'
            }
        });
        $('#endDate').datepicker({
            todayHighlight: true,
            format: 'yyyy-mm-dd',
            templates: {
                leftArrow: '<i class="la la-angle-left"></i>',
                rightArrow: '<i class="la la-angle-right"></i>'
            }
        });

        // timepicker
        $('#startTime').timepicker({
            minuteStep: 1,
            showSeconds: false,
            use24hours: true
            // showMeridian: true,
        });

        $('#endTime').timepicker({
            minuteStep: 1,
            showSeconds: false,
            use24hours: true
            // showMeridian: true
        });

        // select2
        $('#league').select2({
            placeholder: "Выберите роль",
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

                starTime: {
                    required: true
                },
                endTime: {
                    required: true
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