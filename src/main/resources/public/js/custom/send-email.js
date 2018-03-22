$(document).ready(function () {
    $("#send-email").on('click', function (e) {
        e.preventDefault();
        var sendUrl = $(this).attr('href');
        swal({text:"Успешно отправлено!"});
        $.ajax({
            type:"GET",
            url: sendUrl,
            data: 'json',
            success:function (data) {
                console.log(data);
            }
        })
    })
})