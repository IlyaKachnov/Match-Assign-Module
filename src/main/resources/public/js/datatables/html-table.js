//== Class definition

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
});