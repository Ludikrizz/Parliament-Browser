<#macro login loggedIn>
    <button class="submitButton" id="loginShow" onclick="loginButton()">User</button>


    <div class="loginForm">
        <#if loggedIn?? && loggedIn>
            <div class="loginFormContainer">
                <a href="/usersettings">Einstellungen</a>
                <button class="submitButton buttonRed" id="logout" onclick="logout()">Logout</button>
            </div>
        <#else>
            <div class="loginFormContainer">

                <label for="username">Username
                    <input id="username" type="text" placeholder="Enter unsername" name="username" required>
                </label>

                <label for="password">Passwort
                    <input id="password" type="password" placeholder="Enter password" name="password" required>
                </label>

                <button class="submitButton" id="login" onclick="login()">Login</button>
            </div>
        </#if>

    </div>


</#macro>