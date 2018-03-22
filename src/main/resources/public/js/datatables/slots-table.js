//== Class definition
var SweetAlertTable = function () {
    var initAlert = function () {
        var alert = $(document).on('click', '#m_sweetalert', function(e) {
            e.preventDefault();
            var href = $(this).attr("data-href");
            var nRow = $(this).closest('tr');
            var token = $("meta[name='_csrf']").attr("content");

            swal({
                title: 'Удалить запись?',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Удалить',
                cancelButtonText: "Отмена",
            }).then(function(result) {
                if (result.value) {
                    $.ajaxSetup({
                        headers: {
                            "X-CSRF-TOKEN": token,
                        },
                    });
                    $.ajax({
                        type: "POST",
                        url: href,
                        success: function () {
                            nRow.find('td').remove();
                            swal(
                                'Удалено!',
                                'Запись была успешно удалена',
                                'success'
                            )
                        }
                    });
                }
            });
        });
    };

    return {
        //== Public functions
        init: function() {
            initAlert();
        },
    };
}();
var DatatableHtmlTableDemo = function() {
    //== Private functions

    // demo initializer
    var demo = function() {

        var datatable = $('.m-datatable').mDatatable({
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
            data: {
                saveState: {cookie: false},
            },
            search: {
                input: $('#generalSearch'),
            },
            columns: [
                {
                    field: "Статус",
                    title: "Статус",
                },
                {
                    field: "Действия",
                    title: "Действия",
                    sortable: false,
                },
            ],
        });
    };

    return {
        //== Public functions
        init: function() {
            // init dmeo
            demo();
        },
    };
}();

jQuery(document).ready(function() {
    DatatableHtmlTableDemo.init();
    SweetAlertTable.init();
});