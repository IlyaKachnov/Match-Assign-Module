//== Class definition

var SweetAlertTable = function () {
    var initAlert = function () {
        var alert = $(document).on('click', '.m_sweetalert', function(e) {
            e.preventDefault();
            var thisEl = $(this);
            var href = thisEl.attr("data-href");
            var nRow = thisEl.closest('tr');
            var token = $("meta[name='_csrf']").attr("content");
            var dataAction = thisEl.attr("data-action");
            var title = "Удалить запись?";
            var type = "POST";
            var confirmButtonText = "Удалить";
            var successHeader = 'Удалено!';
            var successText =  'Запись была успешно удалена';
            if(dataAction == 1) {
                title = "Отменить слот?";
                type = "GET";
                confirmButtonText = "Да";
                successHeader = "Слот отменен!";
                successText = "Слот был успешно отменен";
            }

            swal({
                title: title,
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: confirmButtonText,
                cancelButtonText: "Отмена",
            }).then(function(result) {
                if (result.value) {
                    $.ajaxSetup({
                        headers: {
                            "X-CSRF-TOKEN": token,
                        },
                    });
                    $.ajax({
                        type: type,
                        url: href,
                        success: function () {
                            swal(
                                successHeader,
                                successText,
                                'success'
                            )
                            if(dataAction == 1) {
                                // thisEl.find("i").removeClass("la-calendar-times-o")
                                //     .addClass("la-calendar-plus-o");
                                location.reload();
                            }
                            else {
                                nRow.find('td').remove();
                            }
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