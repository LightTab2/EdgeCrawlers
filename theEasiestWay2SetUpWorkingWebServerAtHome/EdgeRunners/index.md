---
layout: default
---
<script src="\assets\jquery\jquery-3.3.1.min.js"> </script>
<script>
function setCookie(name,value,days)
 {
    var expires = "";
    if (days) 
    {
        var date = new Date();
        date.setTime(date.getTime() + (days*24*60*60*1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "")  + expires + "; path=/";
}
function getPostResponse()
{
    var name = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    if (name == "" | password == "")
    {
        alert("Nie wszystkie pola są wypełnione!");
    }
    else
    {
        document.getElementById("login").disabled = true;
        $.ajax(
        {
            "headers": { 
                "Accept": "application/json",
                "Content-Type": "application/json"
            },    
            "dataType": "text",
            "type": "POST",
            "data": "{\"name\" : \"" + name + "\", \"password\" : \"" + password + "\"}",
            "url": "http://150.254.40.14:8080/checkUser",
            "success": function(response)
            {
                if ((response == "Wrong password") || (response == "User does not exist"))
                {
                    alert(response);
                    document.getElementById("login").disabled = false;
                }
                else 
                {
                    setCookie("sessionToken", response, 3);
                    setCookie("userName", name, 3);
                    window.location.href = "/opinnion";
                }
            },
            "error": function(response)
            {
                alert("Connection timeout!");
                document.getElementById("login").disabled = false;
            },
            timeout: 6000
        });
    }
}
</script>

<form style="margin: 0px auto; text-align: center;">
<label>Username</label><br>
<input id="username" type="text" placeholder="user" name="username" required><br>
<label>Password</label><br>
<input id="password" type="password" placeholder="****" name="password" required><br> 
<button id="login" type="button" onclick='getPostResponse()'>Login</button>
</form>