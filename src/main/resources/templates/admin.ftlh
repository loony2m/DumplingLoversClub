<#import "blocks/template.ftlh" as t>
<@t.template user>
    `<h1 style="text-align: center">Панель администратора</h1><hr>
        <a href = "/admin/create" style="width: 100%" class="btn btn-dark">Добавить пользователя</a> <hr>
    <table class="table table-hover table-dark">
        <tr>
            <th scope="col">Логин</th>
            <th scope="col">Роли</th>
            <th scope="col">Удалить</th>
            <th scope="col">Редактирование</th>
        </tr>
        <#list users as user>
            <tr>
                <th>${user.username}</th>
                <th><#list user.roles as role>${role}</#list></th>
                <th>
                    <form action="/admin/user/delete/${user.id}" method="post">
                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                        <input type="submit" class="btn btn-light" value="Удалить">
                    </form>
                </th>
                <th><a href="/admin/edit/${user.id}">Редактировние</a></th>
            </tr>
        <#else>
            <h3>Пользователей нет</h3>
        </#list>
    </table>
    <hr>
    <form action="/admin/create-manufacturer" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <label class="mb-3" for="manufacturerName">Название производителя</label>
        <br>
        <input style="width: 100%" class="mb-3" type="text" id="manufacturerName" name="name">
        <input style="width: 100%" class="btn btn-dark" type="submit" value="Добавить производителя">
    </form>
    <hr>
    <form class="mb-5" action="/admin/create-brand" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <label class="mb-3" for="brandName">Название бренда</label>
        <br>
        <input style="width: 100%" class="mb-3" type="text" id="brandName" name="name">
        <input style="width: 100%" class="btn btn-dark" type="submit" value="Добавить бренд">
    </form>

    <h2>Список производителей</h2>
    <ul class="list-group mb-5">
        <#list manufacturers as manufacturer>
            <li class="list-group-item">${manufacturer.name} <a class="btn btn-danger ml-5" href="/admin/delete-manufacturer/${manufacturer.id}" onclick="event.preventDefault(); document.getElementById('delete-form-${manufacturer.id}').submit();">Удалить</a>
                <form id="delete-form-${manufacturer.id}" action="/admin/delete-manufacturer/${manufacturer.id}" method="post" style="display: none;">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                </form>
            </li>
        </#list>
    </ul>

    <h2>Список брендов</h2>
    <ul class="list-group">
        <#list brands as brand>
            <li class="list-group-item">${brand.name} <a class="btn btn-danger ml-5" href="/admin/delete-brand/${brand.id}" onclick="event.preventDefault(); document.getElementById('delete-form-${brand.id}').submit();">Удалить</a>
                <form id="delete-form-${brand.id}" action="/admin/delete-brand/${brand.id}" method="post" style="display: none;">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                </form>
            </li>
        </#list>
    </ul>

</@t.template>