<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Создание нового пользователя</h1>
    <hr>
    <form action="/admin/create" method="post">
        <div class="form-group">
            <label for="username">Логин</label>
            <input type="text" class="form-control" id="username" required name="username">
        </div>
        <div class="form-group">
            <label for="password">Пароль</label>
            <input type="password" class="form-control" id="password" required name="password">
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit" style="width: 100%" class="btn btn-dark">Создать нового пользователя</button>
    </form>
    <#if errorMessage??>
        <h2 style="color: red">${errorMessage}</h2>
    </#if>
</@t.template>