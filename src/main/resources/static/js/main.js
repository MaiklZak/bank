$(document).ready((function () {
    /*---------------------------------new/change offer page---------------------------------------*/
    $('#tableSchedule').hide();
    function showCountYearsRange() {
        let countMonth = $('#customRange3').val();
        $('#labelRange').text('Срок кредита (лет): ' + (countMonth / 12));
    }

    showCountYearsRange();
    $('#customRange3').change(function () {
        showCountYearsRange();
    });
    let creditAmount = $('#creditAmount');
    $('#selectCredit').change(function (e) {
        let credit = $('#selectCredit :selected');
        if (credit.val() !== 'not') {
            let limit = credit.attr('limit');
            let rate = credit.attr('rate');
            let total = parseFloat(limit) * parseFloat(rate) / 100 + parseFloat(limit);
            creditAmount.val('₽' + total.toFixed(2));
            $('#creditAmountHidden').val(total.toFixed(2));
        } else {
            e.preventDefault();
        }
        $('#defaultOption').hide();
    });

    $('#computeSchedule').click(function () {
        $('#tableSchedule tbody').empty();
        $('#tableSchedule').show();
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
                data.forEach(function (schedule) {
                    rows = rows + '<tr>' +
                        '<td>' + schedule.dateFormat + '</td>' +
                        '<td>' + '₽ ' + schedule.amountPayment + '</td>' +
                        '<td>' + '₽ ' + schedule.bodyRepayment + '</td>' +
                        '<td>' + '₽ ' + schedule.interestRepayment + '</td>' +
                        '</tr>'
                });
                $('#tableSchedule').append(rows);
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
    $('#bodyClientForm > section > form').change(function (){
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

    $('#bodyCreditForm > section > form').change(function (){
        $('#btnClear').show();
    });
}));