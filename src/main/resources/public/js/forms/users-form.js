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
    $('#role').on('select2:change', function(){
        validator.element($(this)); // validate element
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
                teamList: {
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
            initValidator();
        }
    };
}();

jQuery(document).ready(function() {
    FormWidgets.init();
});