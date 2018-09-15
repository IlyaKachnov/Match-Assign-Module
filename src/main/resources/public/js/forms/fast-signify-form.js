//== Class definition

var FormWidgets = function () {
    //== Private functions
    var validator;
    // if(teamList != null){
    //     console.log(JSON.stringify(teamList));
    // }
    var initWidgets = function () {

        $("#m_select2_2").select2({
            placeholder: "Выберите слот",
            allowClear: true
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
                slot: {
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