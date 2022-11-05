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

<script src="\assets\jquery\jquery-3.3.1.min.js"></script>

<!-- <script type = "text/javascript">   -->
<!-- <script>  
    sendOpinion(positive) {   
        alert("how are you");  
    }  
</script>   -->

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
                    for (var row in response){
                        alert(row + "->"+JSON.stringify(response[row]))
                        $("table").append("<tr> <td>"+JSON.stringify(response[row]["url"]).slice(1,-1)+"<td>"+JSON.stringify(response[row]["rating"])+"</td>"+"<td>"+JSON.stringify(response[row]["occurrences"])+"</td> <td> <button class='thumb-up' onclick='sendOpinion(True)'></button> <button class='thumb-down' onclick='sendOpinion(False)'></button></td></tr>")
                        }
                }
            }
        });
</script>
