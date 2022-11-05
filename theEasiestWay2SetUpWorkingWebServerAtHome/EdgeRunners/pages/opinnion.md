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
            "data": "{ \"username\":\"Test1\" , \"url\": \""+ url + "\" , \"ratePositive\": \""+ positive + "\"}",
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
            "url": "http://localhost:8080/urlData",
            "success": function(response)
            {
                for (var row in response){
                    if(response[row]["url"] !== undefined){
                        $("table").append("<tr> <td>"+JSON.stringify(response[row]["url"])+"<td>"+JSON.stringify(response[row]["rating"])+"</td>"+"<td>"+JSON.stringify(response[row]["occurrences"])+"</td> <td> <button class='thumb-up' onclick='sendOpinion(\"true\","+JSON.stringify(response[row]["url"])+","+getCookie('userName')+")'></button> <button class='thumb-down' onclick='sendOpinion(false,"+JSON.stringify(response[row]["url"])+","+getCookie('userName')+")'></button></td></tr>")
                    }
                }
            }
        });
</script>
