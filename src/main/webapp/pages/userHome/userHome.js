
let COMPANY_LIST_URL = buildUrlWithContextPath("company-servlet");

function refreshUsersList(users) {
    //clear all current users
    $("#userTable").empty();
    $('<tr> <th> Name: </th> <th> Type: </th> </tr>' )
        .appendTo($("#userTable"));
    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(index, user) {
        $('<tr> <td> '+ user.userName +'</td> <td> '+(user.isAdmin ? 'admin' : 'worker')+' </td></tr>' )
            .appendTo($("#userTable"));
    });
}
function ajaxUsersList() {
    $.ajax({
        url: USER_LIST_URL,
        method:"get",
        success: function(users) {
            refreshUsersList(users);
        }
    });
}

function ajaxStocksList() {
    $.ajax({
        url: COMPANY_LIST_URL,
        method:"GET",
        error: function(errorObject) {
            alert(errorObject.responseText);
        },
        success: function(stocks) {
            if (stocks.length === 0) {
                let stockDiv = $("#stockDiv");
                stockDiv.empty();
                $("<div>No stocks found</div>").appendTo(stockDiv);
            } else {
                refreshStocksList(stocks);
            }
        }
    });
}
function refreshStocksList(stocks) {
    //clear all current users
    let stockDiv = $("#stockDiv");
    stockDiv.empty();
    $("<table style=\"width:50% ; background-color:seashell\" id=\"stockTable\"></table>").appendTo(stockDiv);
    $("#stockTable").empty();
    $('<tr> <th> Name: </th> <th> Symbol: </th> <th> Price: </th> <th> Cycle: </th> </tr>' )
        .appendTo($("#stockTable"));
    // rebuild the list of users: scan all users and add them to the list of users
    $.each(stocks || [], function(index, stock) {
        $('<tr> <td><a href="../companyManagment/company.html?companyName='
            +stock.rseSymbol
            +'&companySymbol='
            +stock.rseCompanyName+ '">' +
            ''+ stock.rseCompanyName
            +'</a> </td> <td> '+stock.rseSymbol
            +'</td> <td> '+stock.rsePrice
            +'</td> <td> '+stock.rseCycle+' </td></tr>' )
            .appendTo($("#stockTable"));
    });
}

function getCurrUser() {
    $.ajax({
        url: USER_LIST_URL,
        method: "POST",
        success: function(myJSON) {
            let myUser = JSON.parse(myJSON);
            $('<h1>'+myUser.userName+' '+(myUser.isAdmin ? 'admin' : 'worker')+'</h1>')
                .appendTo($("#me"));
            if( myUser.isAdmin)
                $("#userDiv").remove();
        }
    });
}

function loadXmlClicked(event) {
    let file = event.target.files[0];
    let reader = new FileReader();
    reader.onload = function () {
        let content = reader.result;
        $.ajax(
            {
                url: buildUrlWithContextPath('loadFile'),
                data: {
                    file: content
                },
                type: 'GET',
                error: function(errorObject) {
                    alert(errorObject.responseText);
                },
                success: function (str) {
                    ajaxStocksList();
                    alert("Upload successful!");
                    console.log(str);
                }
            }
        );
    };

    $(function (){
        reader.readAsText(file);
    });

}

$(function() {
    checkLoggedIn();
    getCurrUser();
    setInterval(function (){ajaxStocksList()},200);
    setInterval(function (){ajaxUsersList()},200);

    //The users list is refreshed automatically every second
    // setInterval(ajaxUsersList, refreshRate);

    //The chat content is refreshed only once (using a timeout) but
    //on each call it triggers another execution of itself later (1 second later)
    // triggerAjaxChatContent();
});