//== Class definition

var FormWidgets = function () {
    //== Private functions
    var validator;

    var initWidgets = function() {
        // datepicker
        $('#m_datepicker').datepicker({
            todayHighlight: true,
            autoclose: true,
            format: 'yyyy-mm-dd',
            templates: {
                leftArrow: '<i class="la la-angle-left"></i>',
                rightArrow: '<i class="la la-angle-right"></i>'
            }
        });


        // select2
        $('#homeTeam').select2({
            // placeholder: "Выберите хозяев",
        });
        $('#homeTeam').on('select2:change', function(){
            validator.element($(this)); // validate element
        });
        $('#guestTeam').select2({
            // placeholder: "Выберите гостей",
        });
        $('#guestTeam').on('select2:change', function(){
            validator.element($(this)); // validate element
        });

    }

    var initValidation = function () {
        validator = $( "#m_form_1" ).validate({
            // define validation rules
            rules: {
                matchDate: {
                    required: true,
                    date: true
                },
                homeTeam: {
                    required: true
                },
                guestTeam: {
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