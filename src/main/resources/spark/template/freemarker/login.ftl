<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>Login</title>
    <link rel="stylesheet" href="/css/style.css">


</head>

<body>
<div class="page">

  <h1>Web Checkers | SignIn</h1>

  <!-- Provide a navigation bar -->
    <div class="navigation">
        <a href="/">my home</a> |

        <a href="/signin">sign in</a>

     </div>
<div class="body">



    <h2> Username Requirements: </h2>

    <ul>
      <li>Must contain at least one alphanumeric character</li>
      <li>Must be unique from other users</li>
    </ul>

    <h2> Input Username: </h2>

    <#include "message.ftl" />

    <form action="./username" method="POST">
        <input name="playerName" />
        <br/><br/>
        <button type="submit" >submit</button>
    </form>

</div>
</div>
</body>

</html>