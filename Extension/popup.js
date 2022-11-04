$.ajax(
{
    headers: { 
        "Accept": "application/json",
        "Content-Type": "application/json" 
    },
    type: "POST",
    url: "http://localhost:8080/checkSite",
    data: "{}",
    contentType: "application/json",
    dataType: "json",
    success: function(response) 
    {
        alert("Yeah!");
    }
});