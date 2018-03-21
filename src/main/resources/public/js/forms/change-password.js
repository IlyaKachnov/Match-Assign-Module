//== Class definition

var FormControls = function () {
    //== Private functions

    var passwordForm = function () {
        $( "#m_form_1" ).validate({
            // define validation rules
            rules: {
                old_password: {
                    required: true,
                    // minlength: 10
                },
                new_password: {
                    required: true,
                    minlength: 6,
                },
                confirm_password: {
                    required: true,
                    minlength: 6,
                    equalTo: "#new_password",
                },
            },
            messages: {
              old_password: {
                  required: "Поле обязательно для заполнения",
              },
              new_password: {
                  required: "Поле обязательно для заполнения",
                  minlength: "Введите минимум 6 символов",
              },
                confirm_password: {
                    required: "Поле обязательно для заполнения",
                    minlength: "Введите минимум 6 символов",
                    equalTo: "Пароли не совпадают",
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
            passwordForm();
        }
    };
}();

jQuery(document).ready(function() {
    FormControls.init();
});