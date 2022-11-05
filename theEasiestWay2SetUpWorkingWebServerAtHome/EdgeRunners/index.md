---
layout: default
---
<script src="\assets\jquery\jquery-3.3.1.min.js"> </script>
<script>
function getPostResponse()
{
    document.getElementById("login").disabled = true;
    var user = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    if (user == "" | password == "")
        alert("Nie wszystkie pola są wypełnione!");
    $.ajax(
        {
            "headers": { 
                "Accept": "application/json",
                "Content-Type": "application/json"
            },    
            "dataType": "json",
            "type": "POST",
            "data": "{\"user\" : \"" + user + "\", \"password\" : \"" + password + "\"}",
            "url": "http://150.254.40.13:8080/chedas",
            "success": function(response)
            {
                window.location.href = "/opinnion";
            },
            "error": function(response)
            {
                document.getElementById("login").disabled = false;
            },
            timeout: 3000
        });
}
</script>

<form style="margin: 0px auto; text-align: center;">
<label>Username</label><br>
<input id="username" type="text" placeholder="user" name="username" required><br>
<label>Password</label><br>
<input id="password" type="password" placeholder="****" name="password" required><br> 
<button id="login" type="button" onclick='getPostResponse()'>Login</button>
</form>