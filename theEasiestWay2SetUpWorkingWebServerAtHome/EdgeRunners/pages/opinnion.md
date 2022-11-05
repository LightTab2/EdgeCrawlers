---
layout: default
title: Asked for opinnion 
permalink: /opinnion/
---

<table style="margin: 10px auto 0px auto;">
    <tr>
        <th>URL</th>
        <th>Rent</th>
        <th>Occurances</th>
        <th>Thumbs</th>
    </tr>
</table>

<script>  
    function sendOpinion(positive) {   
        $.ajax(
        {
            "headers": { 
                "Accept": "application/json",
                "Content-Type": "application/json"
            },    
            "dataType": "json",
            "type": "POST",
            "url": "http://localhost:8080/addRatetoDB",
            "success": function(response)
            {
                if(positive===true){
                    alert("up");
                }
                else{
                    alert("down");
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
                    $("table").append("<tr> <td>"+JSON.stringify(response[row]["url"]).slice(1,-1)+"<td>"+JSON.stringify(response[row]["rating"])+"</td>"+"<td>"+JSON.stringify(response[row]["occurrences"])+"</td> <td> <button class='thumb-up' onclick='sendOpinion(true)'></button> <button class='thumb-down' onclick='sendOpinion(false)'></button></td></tr>")
                }
            }
        });
</script>
