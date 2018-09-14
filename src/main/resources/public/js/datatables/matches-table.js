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
                    sortable: true

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
        }
    };
}();

jQuery(document).ready(function () {
    DatatableDataLocalDemo.init();
});