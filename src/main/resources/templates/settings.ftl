<html lang="de">
<head>
    <title>InsightBundestag</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <meta charset="UTF-8">
    <meta name="description" content="Visualisierung von Abgeordneten und Reden im Bundestag">
    <meta name="author" content="Genadij Vorontsov">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script src="script/user/userLogin.js"></script>
    <script src="script/user/userAdministration.js"></script>
</head>

<body>

<header>
    <nav>
        <ul>
            <li><a href="/">Home</a></li>
            <li><a href="/abgeordnete">Abgeordnete</a></li>
            <li><a href="/reden">Reden</a></li>
            <li><a href="/charts">Charts</a></li>
        </ul>
    </nav>

    <#import 'login.ftl' as l>
    <@l.login loggedIn=loggedIn!false/>
</header>

<div class="container">
    <#if changePwd?? && changePwd>
        <div class="passwordChanger">
            <h1>Passwort ändern</h1>
            <form id="passwordChangeForm" onsubmit="changePassword(event)">
                <label for="oldPassword">Altes Passwort
                    <input type="password" id="oldPassword" name="oldPassword" required>
                </label>
                <label for="newPassword">Neues Passwort
                    <input type="password" id="newPassword" name="newPassword" required>
                </label>
                <label for="newPasswordRepeat">Neues Passwort wiederholen
                    <input type="password" id="newPasswordRepeat" name="newPasswordRepeat" required>
                </label>
                <input class="submitButton" type="submit" value="Passwort ändern">
            </form>
        </div>

    </#if>
    <#if (deleteUser?? && deleteUser) || (addUser?? && addUser) || (editUser?? && editUser)>
        <div class="users">
            <h1>Benutzer</h1>
            <#if deleteUser?? && deleteUser>
                <div class="usersList">
                    <#list users as user>
                        <div class="user" id="${user.getUsername()}">
                            <label for="username">Benutzername:
                                <input type="text" id="usernameEditForm" name="username" value="${user.getUsername()}" <#if editUser?? && !editUser> readonly </#if>>
                            </label>
                            <label for="newPassword">Neues Passwort:
                                <input type="password" id="newPassword" name="newPassword" <#if editUser?? && !editUser> readonly </#if>>
                            </label>
                            <label for="groups">Gruppe:
                                <select id="groups" name="groups" <#if editUser?? && !editUser> disabled </#if>>
                                    <#list groups as group>
                                        <option value="${group.getName()}" <#if user.getGroup() == group.getName()> selected </#if>>${group.getName()}</option>
                                    </#list>
                                </select>
                            </label>
                            <#if editUser?? && editUser>
                                <button class="submitButton" onclick="saveUserChanges('${user.getUsername()}')">Save</button>
                            </#if>
                            <button class="buttonRed submitButton" onclick="deleteUser('${user.getUsername()}')">Delete</button>
                        </div>
                    </#list>
                </div>
            </#if>
            <#if addUser?? && addUser>
                <div class="user">
                    <label for="username">Benutzername:
                        <input type="text" id="usernameEditFormAdd" name="username" required>
                    </label>
                    <label for="newPassword">Neues Passwort:
                        <input type="password" id="newPasswordAdd" name="newPassword" required>
                    </label>
                    <label for="groups">Gruppe:
                        <select id="groupsAdd" name="groups">
                            <#list groups as group>
                                <option value="${group.getName()}">${group.getName()}</option>
                            </#list>
                        </select>
                    </label>
                    <span style="width: 120px"></span>
                    <button class="submitButton" onclick="addUser()">Add</button>
                </div>
            </#if>
        </div>
    </#if>

    <#if (addGroup?? && addGroup) || (deleteGroup?? && deleteGroup) || (editGroup?? && editGroup)>
        <div class="users">
            <h1>Gruppen</h1>
            <#if deleteGroup?? && deleteGroup>
                <div class="usersList">
                    <#list groups as group>
                        <div class="user" id="${group.getName()}">
                            <label for="groupname">Gruppenname:
                                <input type="text" id="groupname" name="groupname" value="${group.getName()}" <#if editGroup?? && !editGroup> readonly </#if>>
                            </label>
                            <div class="rights">
                                <label for="rights">Rechte:
                                    <select id="rights" name="rights" <#if editGroup?? && !editGroup> disabled </#if> multiple>
                                        <#list allRights as right>
                                            <option value="${right}" <#if group.getRights()?seq_contains(right)> selected </#if>>${right}</option>
                                        </#list>
                                    </select>
                                </label>
                            </div>
                            <#if editGroup?? && editGroup>
                                <button class="submitButton" onclick="saveGroupChanges('${group.getName()}')">Save</button>
                            </#if>
                            <button class="buttonRed submitButton" onclick="deleteGroup('${group.getName()}')">Delete</button>
                        </div>
                    </#list>
                </div>
            </#if>
            <#if addGroup?? && addGroup>
                <div class="user" id="addGroup">
                    <label for="groupname">Gruppenname:
                        <input type="text" id="groupnameAdd" name="groupname" required>
                    </label>
                    <div class="rights">
                        <label for="rights">Rechte:
                            <select id="rightsAdd" name="rights" multiple>
                                <#list allRights as right>
                                    <option value="${right}">${right}</option>
                                </#list>
                            </select>
                        </label>
                    </div>
                    <span style="width: 120px"></span>
                    <button class="submitButton" onclick="addGroup()">Add</button>
                </div>
            </#if>
        </div>
    </#if>

</div>

</body>

</html>
