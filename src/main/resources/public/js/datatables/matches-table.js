//== Class definition

var DatatableDataLocalDemo = function () {
    //== Private functions

    // demo initializer
    var demo = function () {

        var dataJSONArray = JSON.parse(matches);
        var tours = JSON.parse(tourData);
        var leagues = JSON.parse(leagueData);
        var datatable = $('.m_datatable').mDatatable({
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
            // datasource definition
            data: {
                type: 'local',
                source: dataJSONArray,
                pageSize: 10
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

            // inline and bactch editing(cooming soon)
            // editable: false,

            // columns definition
            columns: [
                {
                    field: "Home",
                    title: "Хозяева"
                }, {
                    field: "Guest",
                    title: "Гости",
                }, {
                    field: "MatchDate",
                    title: "Дата матча",
                    type: "date",
                    format: "DD.MM.YYYY"
                },
                {
                    field: "Tour",
                    title: "Тур",
                    // callback function support for column rendering
                    template: function (row) {
                        return '<span>' + tours[row.Tour].title + '</span>';
                    }
                },
                {
                    field: "League",
                    title: "Лига",
                    // callback function support for column rendering
                    template: function (row) {
                        return '<span>' + leagues[row.League].title + '</span>';
                    }
                }]
        });

        var query = datatable.getDataSourceQuery();

        $('#m_form_league').on('change', function () {
            datatable.search($(this).val(), 'League');
        }).val(typeof query.League !== 'undefined' ? query.League : '');

        $('#m_form_tour').on('change', function () {
            datatable.search($(this).val(), 'Tour');
        }).val(typeof query.Tour !== 'undefined' ? query.Tour : '');

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