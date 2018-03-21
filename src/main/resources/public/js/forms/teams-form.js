//== Class definition

var FormControls = function () {
    //== Private functions

    var leaguesForm = function () {
        $( "#m_form_1" ).validate({
            // define validation rules
            rules: {
                name: {
                    required: true,
                    // minlength: 10
                },
            },
            messages: {
                name: {
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
            leaguesForm();
        }
    };
}();

jQuery(document).ready(function() {
    FormControls.init();
});