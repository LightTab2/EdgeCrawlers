---
layout: default
title: Asked for opinnion 
permalink: /opinnion/
---

<table style="margin: 10px auto 0px auto;">
    <tr>
        <th>URL</th>
        <th>Customer ID</th>
        <th>Rent</th>
        <th>Thumbs</th>
    </tr>
</table>

<script src="\assets\jquery\jquery-3.3.1.min.js"></script>

<!-- <script type = "text/javascript">   -->
<script>  
    sendOpinion(positive) {   
        alert("how are you");  
    }  
</script>  

<script>
    $.ajax(
        {
            "headers": { 
                "Accept": "application/json",
                "Content-Type": "application/json"
            },    
            "dataType": "json",
            "type": "GET",
            "url": "http://localhost:8080/opinnionsData",
            "success": function(response)
            {
                for (var i = 0; i != 2; ++i)
                $("table").append("
                <tr> <td>ame</td> 
                <td>Cusomer ID</td> 
                <td>Ret</td> 
                <td> <button class='thumb-up' onclick="sendOpinion(True)"></button> 
                <button class='thumb-down' onclick="sendOpinion(False)"></button></td></tr>")
            }
        });
</script>
