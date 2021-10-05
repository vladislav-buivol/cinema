function seats() {
    return function (hall) {
        let cell = hall.cell;
        let row = hall.row;
        let hallId = hall.id;
        let timeOut = hall.refreshTime;
        let seatTable = document.getElementById("seats");
        let tickets = hall.unavailablePlaces;
        let unavailablePlaces = [];
        for (let t in tickets) {
            unavailablePlaces.push(new Seat(tickets[t].row, tickets[t].cell));
        }
        addDataToThead(cell, seatTable);
        addDataToTbody(row, seatTable, cell, hallId, unavailablePlaces);
        let timer = new Timer(timeOut);
        timer.start();
        setTimeout(function () {
            location.reload();
        }, timeOut);
    };
}

$(document).ready(
    function getSeats(id) {
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/cinema/hall',
            data: {"id": 2},
            dataType: 'json'
        }).done(seats()).fail(function (err) {

        }).fail(function (err) {
            console.log(err);
        });
    }
)

function addDataToThead(cell, seatTable) {
    let thead = document.createElement("thead");
    let header = thead.insertRow(0);
    let th = document.createElement("th");
    th.textContent = "Ряд / Место";
    header.appendChild(th);
    for (let c = 1; c <= cell; c++) {
        let b = document.createElement("th");
        b.textContent = c.toString();
        header.appendChild(b);
    }
    thead.appendChild(header);
    seatTable.appendChild(thead);

}

function addDataToTbody(row, seatTable, cell, hallId, unavailablePlaces) {
    let tbody = document.createElement("tbody");
    for (let r = 1; r <= row; r++) {
        let newRow = tbody.insertRow(-1);
        let nr = newRow.insertCell(-1);
        let b = document.createElement("b");
        b.textContent = r.toString();
        nr.appendChild(b);
        for (let c = 1; c <= cell; c++) {
            let seat = new Seat(r, c);
            addSeatFieldToRow(newRow, seat, hallId, isBusy(seat, unavailablePlaces));
        }
    }
    seatTable.appendChild(tbody);
}

function addSeatFieldToRow(newRow, seat, hallId, isDisabled) {
    let newCell = newRow.insertCell(seat.cell);
    let newText = document.createTextNode(`Ряд ${seat.row}, Место ${seat.cell}`);
    let radioInput = document.createElement("input");
    radioInput.type = "radio";
    radioInput.name = "place";
    if (isDisabled) {
        radioInput.disabled = isDisabled;
        newCell.style.background = 'grey';
    }
    radioInput.value = `${seat.row}-${seat.cell}-${hallId}`;
    newCell.appendChild(radioInput);
    newCell.appendChild(newText);
    return newCell;
}

class Seat {
    constructor(row, cell) {
        this.row = row;
        this.cell = cell;
    }


}

class Timer {
    constructor(timeout) {
        this.countDownDate = new Date(new Date().getTime() + timeout);
    }

// Update the count down every 1 second
    start() {
        let countDownDate = this.countDownDate;
        let x = setInterval(function () {
            // Get today's date and time
            let now = new Date().getTime();

            // Find the distance between now and the count down date
            let distance = countDownDate - now;

            // Time calculations for days, hours, minutes and seconds
            let minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            let seconds = Math.floor((distance % (1000 * 60)) / 1000);

            // Display the result in the element with id="demo"
            document.getElementById("timer").innerHTML = minutes + "m " + seconds + "s ";

            // If the count down is finished, write some text
            if (distance < 0) {
                clearInterval(x);
                document.getElementById("timer").innerHTML = "EXPIRED";
            }
        }, 1000);
    }
}

function redirectToPaymentPost() {
    let place = document.querySelector('input[name="place"]:checked');
    if (place != null) {
        let currentUrl = window.location.href;
        if (currentUrl.includes("index")) {
            let currentUrl = window.location.href;
            let indexStart = currentUrl.indexOf("index");
            currentUrl = currentUrl.substring(0, indexStart);
        }
        let row = place.value.split("-")[0];
        let cell = place.value.split("-")[1];
        let hallId = place.value.split("-")[2];
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/cinema/payment',
            data: JSON.stringify({
                row: row,
                cell: cell,
                hallId: hallId
            }),
            dataType: 'text'
        }).done(function () {
            window.location.href = `${currentUrl}payment`;
        }).fail(function (err) {
            console.log(err);
        });
    } else {
        alert("Пожалуйста выберите место")
    }
}

function isBusy(seat, unavailablePlaces) {
    for (let s in unavailablePlaces) {
        if (unavailablePlaces[s].row == seat.row && unavailablePlaces[s].cell == seat.cell) {
            return true;
        }
    }
    return false;

}