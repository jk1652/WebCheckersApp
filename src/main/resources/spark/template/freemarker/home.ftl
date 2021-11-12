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

    <p>
        ${welcome.text}
    </p>

    <#if playerList??>
    	<p>Enter opponent name:</p>
    	<form action="./game" method="POST">
            <input name="opponentName" />
            <button type="submit">Ok</button>
        </form>
        <form action="./game" method="POST">
            <button type="submit" name="AI">Play AI?</button>
        </form>

        <p>
            ${playerListTitle}
            <ul>
            ${playerList}
            </ul>
        </p>
    </#if>

    <!-- Provide a message to the user, if supplied. -->
    <#include "message.ftl" />


    <!-- TODO: future content on the Home:
            to start games,
            spectating active games,
            or replay archived games
    -->

  </div>

</div>
</body>

</html>
