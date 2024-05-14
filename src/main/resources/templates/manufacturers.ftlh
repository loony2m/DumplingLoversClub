<h1>Manufacturers</h1>
<table>
    <thead>
    <tr>
        <th>Name</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <#list manufacturers as manufacturer>
        <tr>
            <td>${manufacturer.name}</td>
            <td>
                <form action="/admin/delete-manufacturer/${manufacturer.id}" method="post">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                    <button type="submit">Delete</button>
                </form>
            </td>
        </tr>
    </#list>
    </tbody>
</table>
