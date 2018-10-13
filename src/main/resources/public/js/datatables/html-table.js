//== Class definition

var SweetAlertTable = function () {
    var initAlert = function () {
        var alert = $(document).on('click', '.m_sweetalert', function (e) {
            e.preventDefault();
            var thisEl = $(this);
            var href = thisEl.attr("data-href");
            var dataAction = thisEl.attr("data-action");
            var title = "Удалить запись?";
            var confirmButtonText = "Удалить";
            if (dataAction == 1) {
                title = "Отменить слот?";
                confirmButtonText = "Да";
            }

            swal({
                title: title,
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: confirmButtonText,
                cancelButtonText: "Отмена",
            }).then(function (result) {
                if (result.value) {
                 window.location.href = href;
                }
            });
        });
    };

    return {
        //== Public functions
        init: function () {
            initAlert();
        },
    };
}();

var DatatableHtmlTableDemo = function () {
    //== Private functions

    // demo initializer
    var demo = function () {

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
                    field: "Действия",
                    title: "Действия",
                    sortable: false,
                },
            ],
        });
    };

    return {
        //== Public functions
        init: function () {
            // init dmeo
            demo();
        },
    };
}();

jQuery(document).ready(function () {
    DatatableHtmlTableDemo.init();
    SweetAlertTable.init();
});