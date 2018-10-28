var MessageClass = function () {
    return {
        init: function () {

            var form = $("#message-form");
            var modal = $("#m_modal_4");
            var token = $("meta[name='_csrf']").attr("content");

            var initAjax = function () {
                $.ajaxSetup({
                    headers: {
                        "X-CSRF-TOKEN": token,
                    },
                });
            };

            $(document).on("click", ".add-msg", function () {
                var action = $(this).data("action");
                var slotBody = $(this).closest(".fc-content");
                var slot = slotBody.find(".fc-time");
                var href = $(this).attr("href");
                var listItem = $(".fc-list-item-title a").find('[href="' + href + '"]').closest(".fc-list-item-title");
                // var form = $("#message-form");
                // var modal = $("#m_modal_4");
                // var token = $("meta[name='_csrf']").attr("content");

                $(document).on('click', '#submit-form', function () {
                    var text = $("#message-text").val();
                    initAjax();
                    $.ajax({
                        type: "POST",
                        data: form.serialize(),
                        url: action,
                        success: function () {
                            modal.modal("hide");
                            setTimeout(function () {
                                location.reload();
                            }, 100);
                            // slot.append('<span tabindex="0" role="button" class="m-badge m-badge--danger" ' +
                            //     'data-toggle="m-popover" data-trigger="focus" data-content="' + text + '" data-original-title="Сообщение">!</span>');
                            // $("a.add-msg", slotBody).remove();
                            // listItem.append('<span tabindex="0" role="button" class="m-badge m-badge--danger" ' +
                            //     'data-toggle="m-popover" data-trigger="focus" data-content="' + text + '" data-original-title="Сообщение">!</span>');
                            // listItem.find(".add-msg").remove();
                            // slot.find(".m-badge").popover();
                        }
                    });
                });


            });

            $(document).on("click", ".delete-msg", function (e) {
                var deleteHref = $(this).data("href");
                // var slotBody = $(this).closest(".fc-content");
                // var listItem = $(".fc-list-item-title a").find('[href="' + deleteHref + '"]').closest(".fc-list-item-title");
                e.preventDefault();
                swal({
                    title: 'Удалить?',
                    type: 'warning',
                    showCancelButton: true,
                    confirmButtonText: 'Удалить',
                    cancelButtonText: "Отмена",
                }).then(function(result) {
                    if (result.value) {
                        initAjax();
                        console.log(deleteHref);
                        $.ajax({
                            type:"POST",
                            url:deleteHref,
                            success: function () {
                                // $(".m-badge", slotBody).remove();
                                // $(".m-badge", listItem).remove();
                                setTimeout(function () {
                                    location.reload();
                                }, 100);
                            }
                        })
                    }
                });

            });

        }

    }
}();

jQuery(document).ready(function () {
    MessageClass.init();
});