//== Class definition

var FormWidgets = function () {
    //== Private functions
    var validator;

    var initWidgets = function() {

        // select2
        $('#matches').select2({
            // placeholder: "Выберите хозяев",
        });
        $('#matches').on('select2:change', function(){
            validator.element($(this)); // validate element
        });
    }

    var initValidation = function () {
        validator = $( "#m_form_1" ).validate({
            // define validation rules
            rules: {
                matches: {
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