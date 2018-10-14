//== Class definition

var DatatableDataLocalDemo = function () {
    //== Private functions

    // demo initializer
    var demo = function () {

        var dataJSONArray = JSON.parse(matches);
        var tours = JSON.parse(tourData);
        var leagues = JSON.parse(leagueData);
        var datatable = $('.m_datatable').mDatatable({
            // datasource definition
            data: {
                type: 'local',
                source: dataJSONArray,
                pageSize: 10,
            },

            // layout definition
            layout: {
                theme: 'default', // datatable theme
                class: '', // custom wrapper class
                scroll: false, // enable/disable datatable scroll both horizontal and vertical when needed.
                // height: 450, // datatable's body's fixed height
                footer: false // display/hide footer
            },

            // column sorting
            sortable: true,

            pagination: true,

            search: {
                input: $('#generalSearch')
            },
            order: [[2, "asc"]],
            translate: {
                records: {
                    processing: 'Загрузка...',
                    noRecords: 'Ничего не найдено'
                },
                toolbar: {
                    pagination: {
                        items: {
                            default: {
                                first: 'Первый',
                                prev: 'Предыдущий',
                                next: 'Следующий',
                                last: 'Последний',
                                more: 'Еще',
                                input: 'Номер страницы',
                                select: 'Показать на странице'
                            },
                            info: 'Показано {{start}} - {{end}} из {{total}} записей'
                        }
                    }
                }
            },

            // inline and bactch editing(cooming soon)
            // editable: false,

            // columns definition
            columns: [
                {
                    field: "home",
                    title: "Хозяева",
                }, {
                    field: "guest",
                    title: "Гости",
                }, {
                    field: "matchDate",
                    title: "Дата матча",
                    type: "date",
                    format: "DD.MM.YYYY",
                    order: "desc"

                },
                {
                    field: "league",
                    title: "Лига",
                    // callback function support for column rendering
                    template: function (row) {
                        return '<span>' + leagues[row.league].title + '</span>';
                    }
                },
                {
                    field: "tour",
                    title: "Тур",
                    // callback function support for column rendering
                    template: function (row) {
                        return '<span>' + tours[row.tour].title + '</span>';
                    }
                },
                {
                    field: "stadium",
                    title: "Стадион",
                    sortable: true
                },
                {
                    field: "delayed",
                    title: "Переносимый",
                    sortable: true
                },
                {
                    field: "message",
                    title: "Сообщение",
                    sortable: false
                },

          ]
        });

        var query = datatable.getDataSourceQuery();

        $('#m_form_league').on('change', function () {
            datatable.search($(this).val(), 'league');
        }).val(typeof query.league !== 'undefined' ? query.league : '');

        $('#m_form_tour').on('change', function () {
            datatable.search($(this).val(), 'tour');
        }).val(typeof query.tour !== 'undefined' ? query.tour : '');

        $('#m_form_league, #m_form_tour').selectpicker();

    };

    return {
        //== Public functions
        init: function () {
            // init dmeo
            demo();
            $(function () {
              $('[data-toggle="m-popover"]').popover()
            })
        }
    };
}();

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
    DatatableDataLocalDemo.init();
    MessageClass.init();
});