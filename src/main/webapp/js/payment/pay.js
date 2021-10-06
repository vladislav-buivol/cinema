function goBack() {
    window.history.go(-1);
}

function pay(r, c, id) {
    if (isEmpty($("#username").val()) || isEmpty($("#phone").val())) {
        alert("Пожалуйста заполните все поля")
    } else {
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/cinema/buy',
            //dataType: 'text'
            data: JSON.stringify({
                row: r,
                cell: c,
                hallId: id
            }),
            dataType: 'text'
        }).done(function () {
            alert("Билет куплен");
            goBack();
        }).fail(function (err) {
            alert("Это место уже занято ");
            goBack();
            console.log(err);
        });
    }
}

function isEmpty(value) {
    return !value.trim().length;
}