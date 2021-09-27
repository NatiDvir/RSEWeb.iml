let MONEY_URL = buildUrlWithContextPath("money-servlet");
let COMPANY_URL = buildUrlWithContextPath("company-servlet");
$(function() { // onload...do
    //add a function to the submit event
    $("#loadMoneyFrom").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            url: MONEY_URL,
            method:this.method,
            timeout: 2000,
            error: function(errorObject) {
               alert(errorObject.responseText);
            },
            success: function(nextPageUrl) {
                console.log(nextPageUrl);
                getMoneyHistory();
            }
        });

        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
    $("#createListingForm").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            url: COMPANY_URL,
            method:this.method,
            timeout: 2000,
            error: function(errorObject) {
                alert(errorObject.responseText);
                console.error("Failed to login !");
            },
            success: function(res) {
                alert(res);

                console.log(res);
                getMoneyHistory();
            }
        });

        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
    checkAdmin();
    setInterval(function () {
        getMoneyHistory()
    },200);
});

function getMoneyHistory(){
    $.ajax({
        url: MONEY_URL,
        method: "get",
        timeout: 2000,
        error: function(errorObject) {
            console.error(errorObject.responseText);
        },
        success: function(resString) {
            let mydiv = $("#moneyHistoryDiv");
            mydiv.empty()
            if(resString.length === 0){
                $("#moneyHistory").remove();
                $("<div>No money history found</div>").appendTo(mydiv);
                let balanceDiv = $("#userbalance");
                balanceDiv.empty();
                $('<span>0</span>').appendTo(balanceDiv);

            }
            else{
                let price = 0;
                let type = "";
                $("<table style=\"background-color:seashell\" id=\"moneyHistory\">\n</table>").appendTo(mydiv);
               let moneyHistory = $("#moneyHistory");
                $('<tr><th style="color:darkblue">Type</th>'
                    + '<th style="color:darkblue">Date</th>'
                    + '<th style="color:darkblue">Amount</th>'
                    + '<th style="color:darkblue">Balance before action</th>'
                    + '<th style="color:darkblue">Balance after action</th>'
                    + '</tr>')
                    .appendTo(moneyHistory);
                // rebuild the list of users: scan all users and add them to the list of users
                $.each(resString || [], function (index, action) {
                    if (action.actionType === 1)
                        type = "Load";
                    if (action.actionType === 2)
                        type = "Buy"
                    if (action.actionType === 3)
                        type = "Sell";
                    $('<tr> <td> '
                        + type
                        + '</td> <td> '
                        + action.actionDate
                        + ' </td><td>'
                        + action.amount
                        + ' </td><td>'
                        + action.balanceBefore
                        + ' </td><td>'
                        + action.balanceAfter
                        + ' </td></tr>')
                        .appendTo(moneyHistory);
                    price = action.balanceAfter;
                });

                let balanceDiv = $("#userbalance");
                balanceDiv.empty();
                $('<span>' + price + '</span>').appendTo(balanceDiv);
            }
        }
    })
}
function checkAdmin() {
    $.ajax({
        url: USER_LIST_URL,
        method: "POST",
        success: function(myJSON) {
            let myUser = JSON.parse(myJSON);
            if( myUser.isAdmin)
                window.location.replace('../../');
        }
    });
}