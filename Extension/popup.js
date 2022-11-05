$.ajax(
{
    "headers": { 
        "Accept": "application/json",
        "Content-Type": "application/json"
    },    
    "dataType": "text",
    "type": "POST",
    "url": "http://localhost:8080/checkSite",
    "success": function(response) 
    {
        var json = $.parseJSON(response);
        $("#percent").html(json["percent"]);
    }
});