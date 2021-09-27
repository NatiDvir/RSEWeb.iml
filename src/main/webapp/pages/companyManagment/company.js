let COMPANY_URL = buildUrlWithContextPath("singleCompany-servlet");
let Comp;
$(function() {
    $("#tradeForm").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            url: COMPANY_URL,
            method:"POST",
            timeout: 2000,
            error: function(errorObject) {
                alert(errorObject.responseText);
                console.error(errorObject.responseText);
            },
            success: function(res) {
                arr = JSON.parse(res);
                let resStr = "";
                $.each(arr || [], function (index, action) {
                    resStr += action +"\n";
                });
                alert(resStr);
                console.log(res);
            }
        });

        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });

    //set hidden fields
    var url = document.location.href,
        params = url.split('?')[1].split('&'),
        data = {}, tmp;
    for (var i = 0, l = params.length; i < l; i++) {
        tmp = params[i].split('=');
        data[tmp[0]] = tmp[1];
    }
    document.getElementById('companySymbolHidden').setAttribute('value',data['companySymbol']);
    document.getElementById('companyNameHidden').setAttribute('value',data['companyName']);
    checkLoggedIn();
    getCurrUser();
    Comp = data['companyName'];
    $('<h1>'+data['companySymbol']+'</h1><h3>'+ data['companyName']+'</h3>').appendTo($("#nameHeader"));
    setInterval(function () {
        fillDealsMade()
    },200);

});

function printDealsMade(arr){
    let companyDealsDiv = $("#companyDeals");
    $( "<tr><th style=\"color:darkblue\">Date</th><th style=\"color:darkblue\">Amount</th><th style=\"color:darkblue\">Price</th></tr>").appendTo(companyDealsDiv);
    $.each(arr || [], function (index, action) {
        $('<tr> <td>' + action.dateOfDeal
            +'</td> <td> '+action.amount
            +'</td> <td> '+action.price
            +'</td></tr>' ).appendTo(companyDealsDiv);
    });
}


function printDealsBuy(arr){
    let companyDealsBuyDiv = $("#companyDealsBuy");
    $( "<tr><th style=\"color:darkblue\">Date</th><th style=\"color:darkblue\">Amount</th><th style=\"color:darkblue\">Price</th></tr>").appendTo(companyDealsBuyDiv);
    $.each(arr || [], function (index, action) {
        $('<tr> <td>' + action.dateOfDeal
            +'</td> <td> '+action.amount
            +'</td> <td> '+action.price
            +'</td></tr>' ).appendTo(companyDealsBuyDiv);
    });
}

function printDealsSell(arr){
    let companyDealsSellDiv = $("#companyDealsSell");
    $( "<tr><th style=\"color:darkblue\">Date</th><th style=\"color:darkblue\">Amount</th><th style=\"color:darkblue\">Price</th></tr>").appendTo(companyDealsSellDiv);
    $.each(arr || [], function (index, action) {
        $('<tr> <td>' + action.dateOfDeal
            +'</td> <td> '+action.amount
            +'</td> <td> '+action.price
            +'</td></tr>' ).appendTo(companyDealsSellDiv);
    });

}
function fillDealsMade() {
    $.ajax({
        data: {
            companyName: Comp
        },
        url: COMPANY_URL,
        method: "GET",
        timeout: 2000,
        error: function (errorObject) {
            console.error(errorObject.responseText);
        },
        success: function (myJSON) {
            let res = myJSON[0];
            if (myJSON.length === 2) {
                let own = $("#stocksOwn");
                own.empty();
                $("<span>" + myJSON[1] + "</span>").appendTo(own);
                document.getElementById('stocksOwnHidden').setAttribute('value', myJSON[1]);
            }
            //let res = JSON.parse(companyJSON);

            let companyCycle = $("#companyCycle");
            let companyPrice = $("#companyPrice");
            companyCycle.empty();
            companyPrice.empty()
            $('<span>' + res.rseCycle + '</span>').appendTo(companyCycle);
            $('<span>' + res.rsePrice + '</span>').appendTo(companyPrice);
            let companyDealsDiv = $("#companyDealsDiv");
            //will return null unlike $("#id")
            let companyDealsBuyDiv = document.getElementById('companyDealsBuyDiv');
            let companyDealsSellDiv = document.getElementById('companyDealsSellDiv');

            companyDealsDiv.empty();
            $("<h3 style=\"color:red\">Deals made</h3>").appendTo(companyDealsDiv);
            if (res.dealsMade.rseOffer === undefined) {
                $("<div>No deals made</div><br/>").appendTo(companyDealsDiv);
            } else {
                if(res.dealsMade.rseOffer.length === 0)
                    $("<div>No deals made</div><br/>").appendTo(companyDealsDiv);

                else
                {
                    $("<table id=\"companyDeals\" style=\"background-color:seashell\" ></table><br/>").appendTo(companyDealsDiv);
                    printDealsMade(res.dealsMade.rseOffer);
                }
            }
            if (companyDealsBuyDiv !== null) {
                companyDealsBuyDiv = $("#companyDealsBuyDiv");
                companyDealsSellDiv = $("#companyDealsSellDiv");
                companyDealsBuyDiv.empty();
                companyDealsSellDiv.empty();
                $("<h3 style=\"color:red\">Deals Awaiting Sale</h3>").appendTo(companyDealsSellDiv);
                $("<h3 style=\"color:red\">Deals Awaiting Buy\n</h3>").appendTo(companyDealsBuyDiv);
                if (res.sellOffers.rseOffer === undefined)
                    $("<div>No deals made</div><br/>").appendTo(companyDealsSellDiv);
                else {
                    if (res.sellOffers.rseOffer.length === 0)
                        $("<div>No deals made</div><br/>").appendTo(companyDealsBuyDiv);
                    else {
                        $("<table id=\"companyDealsSell\" style=\"background-color:seashell\" ></table><br/>").appendTo(companyDealsSellDiv);
                        printDealsSell(res.sellOffers.rseOffer)
                    }
                }
                if (res.buyOffers.rseOffer === undefined)
                    $("<div>No deals made</div><br/>").appendTo(companyDealsBuyDiv);
                else {
                    if (res.buyOffers.rseOffer.length === 0)
                        $("<div>No deals made</div><br/>").appendTo(companyDealsBuyDiv);
                    else {
                        $("<table id=\"companyDealsBuy\" style=\"background-color:seashell\" ></table><br/>").appendTo(companyDealsBuyDiv);
                        printDealsBuy(res.buyOffers.rseOffer)
                    }
                }
            }
        }
    });
}
function getCurrUser() {
    $.ajax({
        url: USER_LIST_URL,
        method: "POST",
        success: function (myJSON) {
            let myUser = JSON.parse(myJSON);
            if (!myUser.isAdmin) {
                $("#companyDealsBuyDiv").remove();
                $("#companyDealsSellDiv").remove();
            }
            else{
                $("#userDiv").empty();
                $("<input type='hidden' id=\"companyNameHidden\"/>").appendTo($("#userDiv"))
            }
        }
    });
}