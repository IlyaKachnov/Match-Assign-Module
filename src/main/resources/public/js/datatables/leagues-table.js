//== Class definition

var DatatableHtmlTableDemo = function() {
    //== Private functions

    // demo initializer
    var demo = function() {

        var datatable = $('.m-datatable').mDatatable({
            data: {
                saveState: {cookie: false},
            },
            search: {
                input: $('#generalSearch'),
            },
            pagination: true,
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
            columns: [{
                field: "Название",
                title: "Название",
                // width: "90%",
            },
                {
                    field: "Действия",
                    title: "Действия",
                    width: 150,
                    sortable: false,
                }
            ]

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
});