chrome.tabs.query({active: true, lastFocusedWindow: true}, tabs => {
    let url = tabs[0].url;
    $.ajax(
        {
            "headers": { 
                "Accept": "application/json",
                "Content-Type": "application/json"
            },    
            "dataType": "json",
            "type": "POST",
            "data": "{\"url\" : \"" + url + "\"}",
            "url": "http://150.254.40.13:8080/checkSite",
            "success": function(response)
            {
                $("#percent").html(response["percent"]);
            }
        });
});