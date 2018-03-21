//== Class definition

var FormWidgets = function () {
    //== Private functions
var validator;
    // bootstrap select
var initWidgets = function () {

    // select2
    $('#role').select2({
        placeholder: "Выберите роль",

    });
    $('#role').on('change', function(){
      if($(this).val() == 'Администратор'){
          $('#teamList').parents().closest('.form-group').css('display', 'none');
      }
      if($(this).val() == 'Менеджер'){
            $('#teamList').parents().closest('.form-group').css('display', 'flex');
        }
    });

    $('#teamList').selectpicker();
    $('#teamList').on('changed.bs.select', function() {
        validator.element($(this)); // validate element
    });

}

    var initValidator = function () {
        validator = $( "#m_form_1" ).validate({
            // define validation rules
            rules: {
                email: {
                    required: true,
                    email: true,
                    minlength: 6
                },
                firstname: {
                    required: true
                },
                lastname: {
                    required: true,
                },
                role: {
                    required: true,
                },
                password: {
                    required: true,
                    minlength: 6
                },
            },
            messages: {
                email: {
                    required: "Поле обязательно для заполнения",
                    email: "Укажите корректный email",
                    minlength: "Введите минимум 6 символов",
                },
                firstname: {
                    required: "Поле обязательно для заполнения",
                },
                lastname: {
                    required: "Поле обязательно для заполнения",
                },
                role: {
                    required: "Необходимо выбрать роль",
                },
                password: {
                    required: "Поле обязательно для заполнения",
                    minlength: "Введите минимум 6 символов",
                }
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
            initValidator();
        }
    };
}();

jQuery(document).ready(function() {
    FormWidgets.init();
});