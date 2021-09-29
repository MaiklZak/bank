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

    /*------------------------------------------------------------------------------------*/

}));