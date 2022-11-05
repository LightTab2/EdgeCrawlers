---
layout: default
title: Asked for opinnion 
permalink: /opinnion/
---
<!-- "data": "{\"username\":\"Test1\",\"url\":\"" + url + "\",\"ratePositive\":\"" + positive + "\"}", -->

<table style="margin: 10px auto 0px auto;">
    <tr>
        <th>URL</th>
        <th>Rent</th>
        <th>Occurances</th>
        <th>Thumbs</th>
    </tr>
</table>
<script>
    function getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
    }
</script>
<script>  

    function sendOpinion(positive,url,username) {   
        $.ajax(
        {
            "headers": { 
                "Accept": "application/json",
                "Content-Type": "application/json"
            },    
            "dataType": "json",
            "data": "{ \"username\":\"Test1\" , \"url\": \""+ url + "\" , \"ratePositive\": \""  + positive + "\"}",
            "type": "POST",
            "url": "http://localhost:8080/addRatetoDB",
            "success": function(response)
            {
                if(positive===true){
                    alert("up"+username);
                }
                else{
                    alert("down"+username);
                }
            }
        }); 
    }  
</script>  

<script src="\assets\jquery\jquery-3.3.1.min.js"></script>

<script>
    $.ajax(
        {
            "headers": { 
                "Accept": "application/json",
                "Content-Type": "application/json"
            },    
            "dataType": "json",
            "type": "GET",
            "url": "http://150.254.40.14:8080/urlData",
            "xhrFields": {
             "withCredentials": true
            },
            "success": function(response)
            {
                if (response["status"] != "ok")
                {
                    alert("Niezgodność cookies: " + response["status"]);
                    window.location.href = "../";
                }
                else
                {
                }
            }
        });
</script>
