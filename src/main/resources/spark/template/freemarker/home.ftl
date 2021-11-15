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
        <p>To challenge an AI, select a difficulty:<p>
        <p>(Hover over a difficulty to get a description of the AI movement)</p>
        <form action="./game" method="POST">
            <button type="submit" style="font-size : 15px" title="(Easy) AI makes moves randomly" name="easy">Easy AI</button>
            <button type="submit" style="font-size : 15px" title="(Medium) AI goes on the offensive and pushes you to jump" name="med">Aggressive AI</button>
            <button type="submit" style="font-size : 15px" title="(Difficult) AI tries to keep pieces and avoids jumps" name="hard">Defensive AI</button>
        </form>
        <#if saveList??>
            <br>
            <h2>Saved Games</h2>
            ${saveList}
        </#if>

        <div class="player_list">
          <p>
            ${playerListTitle}
            <ul>
            ${playerList}
            </ul>
          </p>
        </div>
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
