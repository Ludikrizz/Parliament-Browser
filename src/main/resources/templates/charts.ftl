<html lang="de">

<head>
    <title>InsightBundestag</title>
    <meta charset="UTF-8">
    <meta name="description" content="Visualisierung von Charts fuer nlp verarbeitete Parlamentsdebatten">
    <meta name="author" content="Kimon Raschke">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://d3js.org/d3.v4.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script src="script/searchFunction.js"></script>
    <script src="script/user/userLogin.js"></script>
    <link rel="stylesheet" type="text/css" href="../css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>

<body>
<header>
    <nav>
        <ul>
            <li><a href="/">Home</a></li>
            <li><a href="/abgeordnete">Abgeordnete</a></li>
            <li><a href="/reden">Reden</a></li>
            <li id="current"><a href="/charts">Charts</a></li>
        </ul>
    </nav>

    <#import 'login.ftl' as l>
    <@l.login loggedIn=loggedIn!false/>
</header>

<div class="search-and-calender">
    <div class="date-picker">
        <label for="startDate"></label>
        <input type="date" id="startDate" name="startDate" min="1949-09-07" max="2024-03-24"
               value="2021-01-01" onchange="validateDate()" required>

        <label for="endDate"></label>
        <input type="date" id="endDate" name="endDate" min="1949-09-07" max="2024-03-24"
               value="2024-03-24" onchange="validateDate()" required>
    </div>

    <div class="search-input">
        <input type="text" id="searchInput" placeholder="Search..." value="Tarifparteien">
        <button onclick="writeData(document.getElementById('startDate').value,
            document.getElementById('endDate').value, document.getElementById('searchInput').value)"
                id="searchButton"><i class="fa-solid fa-magnifying-glass"></i></button>
    </div>
</div>

<div id="searchResults" style="user-select: none"></div>
</body>

<script src="script/horizontalBarChart.js"></script>
<script src="script/barChart.js"></script>
<script src="script/bubbleChart.js"></script>
<script src="script/radarChart.js"></script>
<script src="script/sunburstChart.js"></script>

</html>