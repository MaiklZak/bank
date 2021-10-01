$(document).ready((function () {
    /*---------------------------------new/change offer page---------------------------------------*/

    function showCountYearsRange() {
        let countMonth = $('#customRange3').val();
        $('#labelRange').text('Срок кредита (лет): ' + (countMonth / 12));
    }

    showCountYearsRange();

    $(document).on('input', '#customRange3', function () {
        showCountYearsRange();
    });

    $('#selectCredit').change(function (e) {
        $('#defaultOption').hide();
    });

    $('#computeSchedule').click(function () {
        $('#tableSchedule tbody').empty();
        $('#tableSchedule').removeAttr('hidden');
        $('#formAmount').removeAttr('hidden');
        $.ajax({
            url: '/schedule',
            type: 'GET',
            dataType: 'json',
            data: {
                amountCredit: $('#selectCredit :selected').attr('limit'),
                interestRate: $('#selectCredit :selected').attr('rate'),
                countMonth: $('#customRange3').val(),
                datePayment: $('#selectDate').val()
            },
            success: function (data) {
                var rows = '';
                let sumAmount = 0;
                data.forEach(function (schedule) {
                    sumAmount += schedule.amountPayment;
                    rows = rows + '<tr>' +
                        '<td>' + schedule.dateFormat + '</td>' +
                        '<td>' + '₽ ' + schedule.amountPayment + '</td>' +
                        '<td>' + '₽ ' + schedule.bodyRepayment + '</td>' +
                        '<td>' + '₽ ' + schedule.interestRepayment + '</td>' +
                        '</tr>'
                });
                $('#tableSchedule').append(rows);
                $('#creditAmount').val(Math.ceil(sumAmount) + ' руб.');
            }
        });
    });

    /*-----------------------------List clients-------------------------------------------------------*/
    let btnClients = $('#btnClients');
    btnClients.click(function () {
        $.ajax({
            url: '/clients',
            type: 'get',
            dataType: 'JSON',
            data: {
                offset: function () {
                    var oldOffset = btnClients.data('offset');
                    btnClients.data('offset', ++oldOffset);
                    return btnClients.data('offset');
                },
                limit: function () {
                    return btnClients.data('limit');
                }
            },
            success: function (data) {
                if (data < 20) {
                    btnClients.hide();
                }
                var rows = '';
                data.forEach(function (client) {
                    rows = rows + '<tr>' +
                        '<td>' + client.fullName + '</td>' +
                        '<td>' + client.phone + '</td>' +
                        '<td>' + client.email + '</td>' +
                        '<td>' + client.passport + '</td>' +
                        '<td><a class="btn btn-secondary btn-sm" href="/clients/' + client.id + '">Подробнее</a></td>' +
                        '</tr>'
                });
                $('#tableClients').append(rows);
            }
        });
    });

    /*-----------------------------Form client-------------------------------------------------------*/
    $('#phone').mask('+7 (999) 999-99-99');
    $('#passport').mask('99 99 999 999')
    $('#btnClear').hide();
    $('#bodyClientForm > section > form').change(function () {
        $('#btnClear').show();
    });
    $('#mesSuccess').fadeOut(5000);

    /*------------------------------Credit form------------------------------------------------------*/
    function formatNum(num) {
        let strNum = $(num);
        strNum.val(strNum.val().replace('.', ''));

        let suf = strNum.val().substring(strNum.val().length - 2);
        let pre = strNum.val().substring(0, strNum.val().length - 2);

        strNum.val(pre);
        strNum.val(strNum.val().replace(/\s+/g, ''));
        strNum.val(strNum.val().replace(/(\d{1,3})(?=((\d{3})*)$)/g, " $1"));
        strNum.val(strNum.val().replace(/^\s/g, ''));
        strNum.val(strNum.val() + '.' + suf);
    }

    let limitId = $('#limit');
    limitId.bind('keyup', function () {
        formatNum(limitId);
    });

    let inputRate = $('#interestRate');
    if (inputRate.val().length === 4) {
        inputRate.val('0' + inputRate.val());
    }
    inputRate.mask('99.99', {autoclear: false});

    $('#bodyCreditForm > section > form').change(function () {
        $('#btnClear').show();
    });
}));