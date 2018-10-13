//== Class definition
var SweetAlertTable = function () {
    var initAlert = function () {
        var alert = $(document).on('click', '.m_sweetalert', function(e) {
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
                    window.location.href = href;
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
                order: [[1, "asc"]],
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
                saveState: {cookie: true},
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