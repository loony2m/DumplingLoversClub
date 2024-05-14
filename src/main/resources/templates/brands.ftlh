<h1>Brands</h1>
<table>
    <thead>
    <tr>
        <th>Name</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <#list brands as brand>
        <tr>
            <td>${brand.name}</td>
            <td>
                <form action="/admin/delete-brand/${brand.id}" method="post">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                    <button type="submit">Delete</button>
                </form>
            </td>
        </tr>
    </#list>
    </tbody>
</table>
