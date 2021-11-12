<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <meta http-equiv="refresh" content="5">
  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl" />

  <div class="body">

    <p><b>
        ${welcome.text}
    </p></b>

    <#include "message.ftl" />

    <#if playerList??>
    	<p>Enter opponent name to challenge:</p>
    	<form action="./game" method="POST">
            <input name="opponentName" />
            <button type="submit">Ok</button>
        </form>
        <br>
        <form action="./game" method="POST">
            <button type="submit" name="easy">Play easy AI?</button>
            <button type="submit" name="med">Play med AI?</button>
            <button type="submit" name="hard">Play hard AI?</button>
        </form>

        <p>
            ${playerListTitle}
            <ul>
            ${playerList}
            </ul>
        </p>
    </#if>

    <!-- Provide a message to the user, if supplied. -->



    <!-- TODO: future content on the Home:
            to start games,
            spectating active games,
            or replay archived games
    -->

  </div>

</div>
</body>

</html>
