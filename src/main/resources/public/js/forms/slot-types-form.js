//== Class definition

var FormWidgets = function () {
    //== Private functions
    var validator;

    var initWidgets = function() {

        // timepicker
        $('#duration').timepicker({
            minuteStep: 10,
            showSeconds: false,
            showMeridian: false,
            defaultTime: '0:00',

        });


    }

    var initValidation = function () {
        validator = $( "#m_form_1" ).validate({
            // define validation rules
            rules: {

                typeName: {
                    required: true,
                },
                duration: {
                    required: true,
                    time:true,
                },
            },
            messages: {
                typeName: {
                    required: "Поле обязательно для заполнения",
                },
                duration: {
                    required: "Поле обязательно для заполнения",
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