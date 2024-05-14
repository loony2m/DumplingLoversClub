<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Изменение пользователя</h1>
    <hr>
    <form action="/admin/edit/${user.id}" method="post">
        <div class="form-group">
            <label for="username">Логин</label>
            <input type="text" class="form-control" id="username" required name="username">
        </div>
        <div class="form-group">
            <label for="password">Пароль</label>
            <input type="password" class="form-control" id="password" required name="password">
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <input type="hidden" name="userId" value="${user.id}">
        <button type="submit" style="width: 100%" class="btn btn-dark">Сохранить изменения</button>
    </form>

</@t.template>